<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"

                exclude-result-prefixes="xs xsl">

    <xsl:strip-space elements="*"/>

    <xsl:output indent="yes" omit-xml-declaration="yes"/>

    <xsl:variable name="data" select="/Result/data"/>
    <xsl:variable name="entries" select="$data/Entry/Entry"/>
    <xsl:variable name="mixes" select="$data/Mix/Mix"/>
    <xsl:variable name="themes" select="$data/Theme/Theme"/>
    <xsl:variable name="markers" select="$data/Marker/Marker"/>

    <xsl:template match="/">

        <soundtrack>
            <entries>
                <xsl:apply-templates select="$entries" mode="entry"/>
            </entries>
            <persons>
                <xsl:apply-templates select="$markers" mode="person"/>
            </persons>
            <playlists>
                <xsl:apply-templates select="$mixes" mode="playlist"/>
            </playlists>
        </soundtrack>
    </xsl:template>

    <xsl:template match="Entry" mode="entry">

        <!--

            <Entry id="340" created="2012-04-25T13:21:04Z" createdBy="houleo" updated="2012-04-25T13:21:04Z" updatedBy="houleo" unless="x7l0l7s210y7xfv612hrphyksx0ic31e" filename="Entry.340.x7l0l7s210y7xfv612hrphyksx0ic31e.xml" update="true">
                <MixReference id="17"/>
                <Title>Let It Rock</Title>
                <Artist>Kevin Rudolph</Artist>
                <Story>Let It Rock was playing on the radio at the tail end of my travels but it got bigger at home, remaining on the radio and even ascending to TV commercial status. Trav, Justin, and I shared a laugh over adding back in the omitted Li'l Wayne part whenever the commercial aired at fantasy football League Central (Trav's house).</Story>
                <Notes>heard this a bit while traveling, and it remained on the radio and even made it to a commercial by the time i was back home. laura downloaded a copy of this for her workout mix (i think). trav and maybe justin made a joke about this jam at league central, singing the opening to the lil wayne part "wayne's world".

                    something about the experiences i'd just had in louisiana colored my impression of lil wayne, like i had a bit of a sense of where he came from, more than i would have otherwise, anyway.</Notes>
                <TitleIndex>let it rock</TitleIndex>
                <ArtistIndex>kevin rudolph</ArtistIndex>
                <Markers>
                    <MarkerReference id="112"/>
                    <MarkerReference id="58"/>
                </Markers>
                <Spotify>spotify:track:2vVc2G9w2JteBgxpSUVwX5</Spotify>
                <YouTube/>
            </Entry>

        -->

        <entry>
            <xsl:copy-of select="@id"/>
            <playlist-ref id="{MixReference/@id}"/>
            <title><xsl:value-of select="Title"/></title>
            <artist><xsl:value-of select="Artist"/></artist>
            <story><xsl:value-of select="Story"/></story>
            <notes><xsl:value-of select="Notes"/></notes>
            <xsl:apply-templates select="Markers/MarkerReference" mode="entry"/>
            <spotify><xsl:value-of select="Spotify"/></spotify>
            <youtube><xsl:value-of select="YouTube"/></youtube>
        </entry>
    </xsl:template>

    <xsl:template match="MarkerReference" mode="entry">
        <person-ref id="{@id}"/>
    </xsl:template>

    <xsl:template match="Marker" mode="person">

        <!--

            <Marker id="26" unless="m0h63eoa6sd1iopatsgtwoe82td08yx6" filename="Marker.26.m0h63eoa6sd1iopatsgtwoe82td08yx6.xml" update="true">
                <Name>Cook</Name>
                <NameIndex>cook</NameIndex>
                <Type>person</Type>
                <Story>went to same high school, friend and Ultimate teammate since later college</Story>
            </Marker>

        -->

        <person>
            <xsl:copy-of select="@id"/>
            <name><xsl:value-of select="Name"/></name>
            <story><xsl:value-of select="Story"/></story>
        </person>

    </xsl:template>

    <xsl:template match="Mix" mode="playlist">

        <!--

            <Theme id="14" created="2012-04-17T14:43:24-04:00" createdBy="houleo" updated="2012-04-17T14:43:25-04:00" updatedBy="houleo" unless="ousclb6qwjil6cznc7zpk0ctb9j74b7n" filename="Theme.14.ousclb6qwjil6cznc7zpk0ctb9j74b7n.xml" update="true" action="remove">
                <Author>houleo</Author>
                <Published>true</Published>
                <When>2005-12-31</When>
                <ClubReference index="1" id="1"/>
                <Title>2005</Title>
                <Notes>soundtrack of 2005</Notes>
            </Theme>

            <Mix id="13" created="2012-04-17T14:43:24-04:00" createdBy="houleo" updated="2012-04-17T23:18:42-04:00" updatedBy="houleo" unless="wub6h0f4f75yumx7y6px6satm9ec7h50" filename="Mix.13.wub6h0f4f75yumx7y6px6satm9ec7h50.xml" update="true">
                <Author>houleo</Author>
                <ThemeReference index="13" id="13"/>
                <Published>true</Published>
                <Entries>
                    <EntryReference id="241"/>
                    <EntryReference id="242"/>
                    <EntryReference id="243"/>
                    <EntryReference id="244"/>
                    <EntryReference id="245"/>
                    <EntryReference id="246"/>
                    <EntryReference id="247"/>
                    <EntryReference id="248"/>
                    <EntryReference id="249"/>
                    <EntryReference id="250"/>
                    <EntryReference id="251"/>
                    <EntryReference id="252"/>
                    <EntryReference id="253"/>
                    <EntryReference id="254"/>
                    <EntryReference id="255"/>
                    <EntryReference id="256"/>
                    <EntryReference id="257"/>
                    <EntryReference id="258"/>
                    <EntryReference id="259"/>
                    <EntryReference id="260"/>
                </Entries>
                <Story/>
            </Mix>

        -->

        <xsl:variable name="theme" select="$themes[@id = current()/ThemeReference/@id]"/>

        <playlist>
            <xsl:copy-of select="@id"/>
            <year><xsl:value-of select="$theme/Title"/></year>
            <xsl:apply-templates select="Entries/EntryReference" mode="playlist"/>
        </playlist>

    </xsl:template>

    <xsl:template match="EntryReference" mode="playlist">
        <entry-ref id="{@id}"/>
    </xsl:template>

</xsl:stylesheet>
