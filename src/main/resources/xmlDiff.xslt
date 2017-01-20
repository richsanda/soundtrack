<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"

                xmlns:set="http://exslt.org/sets"

                xmlns:primary="primary"
                xmlns:control="control"

                xmlns:util="util"

                exclude-result-prefixes="xsl xs set primary control">

    <!-- xml diff tool

      import this stylesheet from another and call the "compare" template with two args:

	  primary: the root of the primary tree to submit to comparison
	  control: the root of the control tree to compare against

      the two trees will be walked together. the primary tree will be walked in document order, matching elements
      and attributes from the control tree along the way, building a tree of common content, with appendages
      containing primary and control only content. that tree will then be used to generate the diff.

      the process of matching involves finding, for an element or attribute in the primary tree, the
      equivalent element or attribute in the control tree, *at the same level*, and *regardless of ordering*.

	  matching logic is encoded as templates with mode="find-match", providing a hook to wire in specific
	  matching logic for particular elements or attributes. for example, an element may "match" based on an
	  @id attribute value, irrespective of element ordering; encode this in a mode="find-match" template.

	  the treatment of diffs is encoded as templates with mode="primary-only" and "control-only", providing
	  hooks for alternate behavior upon encountering differences.

	  -->

    <xsl:output method="text"/>

    <xsl:strip-space elements="*"/>

    <xsl:param name="full" select="false()"/><!-- whether to render the full doc, as opposed to just diffs -->

    <xsl:template match="/">
        <xsl:call-template name="compare">
            <xsl:with-param name="primary" select="*/*[1]"/><!-- first child of root element, for example -->
            <xsl:with-param name="control" select="*/*[2]"/><!-- second child of root element, for example -->
        </xsl:call-template>
    </xsl:template>

    <!-- OVERRIDES: templates that can be overridden to provide targeted matching logic and diff treatment -->

    <!-- default find-match template for elements

         (by default, for "complex" elements, name has to match, for "simple" elements, name and value do)

         for context node (from "primary"), choose from among $candidates (from "control") which one matches

         (override with more specific match patterns to effect alternate behavior for targeted elements)
         -->
    <xsl:template match="*" mode="find-match" as="element()?">
        <xsl:param name="candidates" as="element()*"/>
        <xsl:choose>
            <xsl:when test="text() and count(node()) = 1"><!-- simple content -->
                <xsl:sequence select="$candidates[node-name(.) = node-name(current())][text() and count(node()) = 1][. = current()][1]"/>
            </xsl:when>
            <xsl:when test="not(node())"><!-- empty -->
                <xsl:sequence select="$candidates[node-name(.) = node-name(current())][not(node())][1]"/>
            </xsl:when>
            <xsl:otherwise><!-- presumably complex content -->
                <xsl:sequence select="$candidates[node-name(.) = node-name(current())][1]"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- default find-match template for attributes

         (by default, name and value have to match)

         for context attr (from "primary"), choose from among $candidates (from "control") which one matches

         (override with more specific match patterns to effect alternate behavior for targeted attributes)
         -->
    <xsl:template match="@*" mode="find-match" as="attribute()?">
        <xsl:param name="candidates" as="attribute()*"/>
        <xsl:sequence select="$candidates[. = current()][node-name(.) = node-name(current())][1]"/>
    </xsl:template>

    <!-- default primary-only template (override with more specific match patterns to effect alternate behavior) -->
    <xsl:template match="@* | *" mode="primary-only">
        <xsl:apply-templates select="." mode="illegal-primary-only"/>
    </xsl:template>

    <!-- write out a primary-only diff -->
    <xsl:template match="@* | *" mode="illegal-primary-only">
        <primary:only>
            <xsl:copy-of select="."/>
        </primary:only>
    </xsl:template>

    <!-- default control-only template (override with more specific match patterns to effect alternate behavior) -->
    <xsl:template match="@* | *" mode="control-only">
        <xsl:apply-templates select="." mode="illegal-control-only"/>
    </xsl:template>

    <!-- write out a control-only diff -->
    <xsl:template match="@* | *" mode="illegal-control-only">
        <control:only>
            <xsl:copy-of select="."/>
        </control:only>
    </xsl:template>

    <!-- end OVERRIDES -->

    <!-- MACHINERY: for walking the primary and control trees together, finding matches and recursing -->

    <!-- compare "primary" and "control" trees (this is the root of comparison, so CALL THIS ONE !) -->
    <xsl:template name="compare">
        <xsl:param name="primary"/>
        <xsl:param name="control"/>

        <!-- write the xml diff into a variable -->
        <xsl:variable name="diff">
            <xsl:call-template name="match-children">
                <xsl:with-param name="primary" select="$primary"/>
                <xsl:with-param name="control" select="$control"/>
            </xsl:call-template>
        </xsl:variable>

        <!-- "print" the xml diff as textual output -->
        <xsl:apply-templates select="$diff" mode="print">
            <xsl:with-param name="render" select="$full"/>
        </xsl:apply-templates>

    </xsl:template>

    <!-- assume primary (context) element and control element match, so render the "common" element and recurse -->
    <xsl:template match="*" mode="common">
        <xsl:param name="control"/>

        <xsl:copy>
            <xsl:call-template name="match-attributes">
                <xsl:with-param name="primary" select="@*"/>
                <xsl:with-param name="control" select="$control/@*"/>
            </xsl:call-template>

            <xsl:choose>
                <xsl:when test="text() and count(node()) = 1">
                    <xsl:value-of select="."/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:call-template name="match-children">
                        <xsl:with-param name="primary" select="*"/>
                        <xsl:with-param name="control" select="$control/*"/>
                    </xsl:call-template>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>

    </xsl:template>

    <!-- find matches between collections of attributes in primary vs control -->
    <xsl:template name="match-attributes">
        <xsl:param name="primary" as="attribute()*"/>
        <xsl:param name="control" as="attribute()*"/>
        <xsl:param name="primaryCollecting" as="attribute()*"/>

        <xsl:choose>
            <xsl:when test="$primary and $control">
                <xsl:variable name="this" select="$primary[1]"/>
                <xsl:variable name="match" as="attribute()?">
                    <xsl:apply-templates select="$this" mode="find-match">
                        <xsl:with-param name="candidates" select="$control"/>
                    </xsl:apply-templates>
                </xsl:variable>

                <xsl:choose>
                    <xsl:when test="$match">
                        <xsl:copy-of select="$this"/>
                        <xsl:call-template name="match-attributes">
                            <xsl:with-param name="primary" select="subsequence($primary, 2)"/>
                            <xsl:with-param name="control" select="remove($control, 1 + count(set:leading($control, $match)))"/>
                            <xsl:with-param name="primaryCollecting" select="$primaryCollecting"/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:call-template name="match-attributes">
                            <xsl:with-param name="primary" select="subsequence($primary, 2)"/>
                            <xsl:with-param name="control" select="$control"/>
                            <xsl:with-param name="primaryCollecting" select="$primaryCollecting | $this"/>
                        </xsl:call-template>
                    </xsl:otherwise>
                </xsl:choose>

            </xsl:when>
            <xsl:otherwise>
                <xsl:if test="$primaryCollecting | $primary">
                    <xsl:apply-templates select="$primaryCollecting | $primary" mode="primary-only"/>
                </xsl:if>
                <xsl:if test="$control">
                    <xsl:apply-templates select="$control" mode="control-only"/>
                </xsl:if>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>

    <!-- find matches between collections of elements in primary vs control -->
    <xsl:template name="match-children">
        <xsl:param name="primary" as="node()*"/>
        <xsl:param name="control" as="element()*"/>

        <xsl:variable name="this" select="$primary[1]" as="node()?"/>

        <xsl:choose>
            <xsl:when test="$primary and $control">
                <xsl:variable name="match" as="element()?">
                    <xsl:apply-templates select="$this" mode="find-match">
                        <xsl:with-param name="candidates" select="$control"/>
                    </xsl:apply-templates>
                </xsl:variable>

                <xsl:choose>
                    <xsl:when test="$match">
                        <xsl:apply-templates select="$this" mode="common">
                            <xsl:with-param name="control" select="$match"/>
                        </xsl:apply-templates>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates select="$this" mode="primary-only"/>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:call-template name="match-children">
                    <xsl:with-param name="primary" select="subsequence($primary, 2)"/>
                    <xsl:with-param name="control" select="if (not($match)) then $control else remove($control, 1 + count(set:leading($control, $match)))"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$primary">
                <xsl:apply-templates select="$primary" mode="primary-only"/>
            </xsl:when>
            <xsl:when test="$control">
                <xsl:apply-templates select="$control" mode="control-only"/>
            </xsl:when>
        </xsl:choose>

    </xsl:template>

    <!-- end MACHINERY -->

    <!-- PRINTERS: print templates for writing out the diff -->

    <xsl:template match="*" mode="print">
        <xsl:param name="depth" select="-1"/>
        <xsl:param name="render" select="false()"/>
        <xsl:param name="lineLeader" select="' '"/>
        <xsl:param name="rest" as="element()*"/>

        <xsl:if test="$render or descendant::primary:* or descendant::control:*">

            <xsl:call-template name="whitespace">
                <xsl:with-param name="indent" select="$depth"/>
                <xsl:with-param name="leadChar" select="$lineLeader"/>
            </xsl:call-template>
            <xsl:text>&lt;</xsl:text>
            <xsl:value-of select="name(.)"/>

            <xsl:apply-templates select="@* | primary:*[@*] | control:*[@*]" mode="print">
                <xsl:with-param name="depth" select="$depth"/>
                <xsl:with-param name="render" select="$render"/>
                <xsl:with-param name="lineLeader" select="$lineLeader"/>
            </xsl:apply-templates>

            <xsl:choose>
                <xsl:when test="text() and count(node()) = 1"><!-- field element (just textual content) -->
                    <xsl:text>&gt;</xsl:text>
                    <xsl:value-of select="."/>
                    <xsl:text>&lt;/</xsl:text>
                    <xsl:value-of select="name(.)"/>
                    <xsl:text>&gt;</xsl:text>
                </xsl:when>
                <xsl:when test="count(node()) = 0"><!-- empty (self-closing) element -->
                    <xsl:text>/&gt;</xsl:text>
                </xsl:when>
                <xsl:otherwise><!-- complex content -->
                    <xsl:text>&gt;&#10;</xsl:text>
                    <xsl:apply-templates select="*[not(self::primary:* and @*) and not(self::control:* and @*)]" mode="print">
                        <xsl:with-param name="depth" select="$depth + 1"/>
                        <xsl:with-param name="render" select="$render"/>
                        <xsl:with-param name="lineLeader" select="$lineLeader"/>
                    </xsl:apply-templates>
                    <xsl:call-template name="whitespace">
                        <xsl:with-param name="indent" select="$depth"/>
                        <xsl:with-param name="leadChar" select="$lineLeader"/>
                    </xsl:call-template>
                    <xsl:text>&lt;/</xsl:text>
                    <xsl:value-of select="name(.)"/>
                    <xsl:text>&gt;</xsl:text>
                </xsl:otherwise>
            </xsl:choose>

            <xsl:text>&#10;</xsl:text>

        </xsl:if>

        <!-- write the rest of the elements, if any -->
        <xsl:apply-templates select="$rest" mode="print">
            <xsl:with-param name="depth" select="$depth"/>
            <xsl:with-param name="render" select="$render"/>
            <xsl:with-param name="lineLeader" select="$lineLeader"/>
            <xsl:with-param name="rest" select="()"/><!-- avoid implicit param pass to recursive call! -->
        </xsl:apply-templates>

    </xsl:template>

    <xsl:template match="@*" mode="print">
        <xsl:param name="depth" select="0"/>
        <xsl:param name="render" select="false()"/>
        <xsl:param name="lineLeader" select="' '"/>
        <xsl:param name="rest" as="attribute()*"/>

        <xsl:if test="$render">

            <xsl:text>&#10;</xsl:text>
            <xsl:call-template name="whitespace">
                <xsl:with-param name="indent" select="$depth + 3"/>
                <xsl:with-param name="leadChar" select="$lineLeader"/>
            </xsl:call-template>
            <xsl:value-of select="name(.)"/>
            <xsl:text>="</xsl:text>
            <xsl:value-of select="."/>
            <xsl:text>"</xsl:text>
        </xsl:if>

        <xsl:apply-templates select="$rest" mode="print">
            <xsl:with-param name="depth" select="$depth"/>
            <xsl:with-param name="render" select="$render"/>
            <xsl:with-param name="lineLeader" select="$lineLeader"/>
            <xsl:with-param name="rest" select="()"/><!-- avoid implicit param pass to recursive call! -->
        </xsl:apply-templates>

    </xsl:template>

    <xsl:template match="primary:* | control:*" mode="print">
        <xsl:param name="depth"/>

        <xsl:variable name="diffType" select="util:diff-type(.)"/>
        <xsl:variable name="primary" select="self::primary:*"/>
        <xsl:variable name="lineLeader" select="if ($primary) then '+' else '-'"/>

        <!-- only if this is the first in a sequence of control::* elements, since the rest are handled along with the first... -->
        <xsl:if test="util:diff-type(preceding-sibling::*[1]) != $diffType">
            <xsl:if test="@*">
                <xsl:text>&#10;</xsl:text>
            </xsl:if>
            <xsl:call-template name="diffspace">
                <xsl:with-param name="indent" select="if (@*) then $depth + 3 else $depth"/>
                <xsl:with-param name="primary" select="$primary"/>
            </xsl:call-template>
            <b><i>&lt;!-- ... --&gt;</i></b><!-- something to identify diff sections in output -->
            <xsl:if test="node()">
                <xsl:text>&#10;</xsl:text>
            </xsl:if>
            <xsl:variable name="rest" select="set:leading(following-sibling::*, following-sibling::*[util:diff-type(.) != $diffType])"/>
            <xsl:apply-templates select="@* | node()" mode="print">
                <xsl:with-param name="depth" select="$depth"/>
                <xsl:with-param name="render" select="true()"/>
                <xsl:with-param name="lineLeader" select="$lineLeader"/>
                <xsl:with-param name="rest" select="$rest/@* | $rest/*"/>
            </xsl:apply-templates>
        </xsl:if>
    </xsl:template>

    <xsl:template name="whitespace">
        <xsl:param name="indent" select="0" as="xs:integer"/>
        <xsl:param name="leadChar" select="' '"/>
        <xsl:choose>
            <xsl:when test="$indent > 0">
                <xsl:value-of select="$leadChar"/>
                <xsl:text> </xsl:text>
                <xsl:for-each select="0 to $indent - 1">
                    <xsl:text>  </xsl:text>
                </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="0 to $indent">
                    <xsl:text>  </xsl:text>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="diffspace">
        <xsl:param name="indent" select="0" as="xs:integer"/>
        <xsl:param name="primary" select="false()"/>
        <xsl:for-each select="0 to $indent">
            <xsl:choose>
                <xsl:when test="$primary">
                    <xsl:text>++</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>--</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>
    </xsl:template>

    <!-- just an "enum" for deciding whether to group adjacent diffs -->
    <xsl:function name="util:diff-type" as="xs:integer">
        <xsl:param name="construct"/>
        <xsl:sequence select="if ($construct/self::primary:*[@*]) then 1 else
                              if ($construct/self::control:*[@*]) then 2 else
                              if ($construct/self::primary:*) then 3 else
                              if ($construct/self::control:*) then 4 else
                              if ($construct) then 5 else 0"/>
    </xsl:function>

    <!-- end PRINTERS -->

</xsl:stylesheet>