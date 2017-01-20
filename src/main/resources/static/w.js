$(pageBehavior);

function pageBehavior () {

    actionsBehavior($(this));

    refreshEntries();
}

function refreshEntries() {

    var url = "/soundtrack";

    $.ajax({
        url: url,
        dataType: "json"
    }).success(function (data) {
        $('#soundtrack').html(showEntries(data));
    });
}

function regionClick (e, behaviorMap) {

    var $$ = $(e.target);
    while ($$.length > 0) {
        for (var key in behaviorMap) {
            if ($$.hasClass(key)) {
                behaviorMap[key]($$);
                return
            }
        }
        $$ = $$.parent();
    }
}

function actionsClick(e) {

    var actions = {
        'add-entry' : function ($$) {
            addEntry($$);
        },
        'edit-entry' : function ($$) {
            editEntry($$);
        },
        'save-entry' : function ($$) {
            saveEntry($$);
        },
        'read-entry' : function ($$) {
            readEntry($$);
        },
        'name-tag' : function ($$) {
            readNameTag($$);
        },
        'hash-tag' : function ($$) {
            readHashTag($$);
        },
        'read-year' : function ($$) {
            readYear($$);
        },
        'no-op' : function ($$) {}
    };

    regionClick(e, actions);
}

function actionsBehavior(root) {

    $(root).on('click touchstart', actionsClick);

    return root
}

function addEntry($$) {
    var data =
    {
        "key" : null,
        "year" : 2016,
        "ordinal" : 0,
        "title" : null,
        "artist" : null,
        "story" : null,
        "notes" : null,
        "spotify" : null,
        "youtube" : null
    }

    var entryDiv = $$.closest('div.entry');
    var overlayDiv = showEntryForEdit(data);
    overlayDiv.width("100%");
    overlayDiv.fadeIn(200);
    entryDiv.append(overlayDiv);
}

function editEntry($$) {

    var entryDiv = $$.closest('div.entry');
    var id = entryDiv.attr('id');
    var url = "/entry/" + id;

    $.ajax({
        url: url,
        dataType: "json"
    }).success(function (data) {
        var overlayDiv = showEntryForEdit(data);
        overlayDiv.width("100%");
        overlayDiv.fadeIn(200);
        entryDiv.append(overlayDiv);
    });
}

function saveEntry($$) {

    var entryDiv = $$.closest('div.entry');
    var id = entryDiv.attr('id');
    var entry = {
        story: entryDiv.find('textarea.edit-story').val(),
        year: entryDiv.find('#year').val(),
        ordinal: entryDiv.find('#ordinal').val(),
        title: entryDiv.find('#title').val(),
        artist: entryDiv.find('#artist').val()
    };

    if (null != id) {
        $.ajax({
            url: "/entry/" + id,
            type: "patch",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(entry)
        }).success(function (data) {
            var newEntryDiv = showEntry(data);
            entryDiv.fadeOut(500, function () {
                entryDiv.replaceWith(newEntryDiv);
                refreshEntries();
            });
        });
    } else {
        $.ajax({
            url: "/entry",
            type: "post",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(entry)
        }).success(function (data) {
            var newEntryDiv = showEntry(data);
            entryDiv.fadeOut(500, function () {
                entryDiv.replaceWith(newEntryDiv);
                newEntryDiv.before(createAddEntryDiv());
            });
        });
    }
}

function readEntry($$) {

    var entryDiv = $$.closest('div.entry');
    var id = entryDiv.attr('id');

    if (!id) {

        entryDiv.replaceWith(createAddEntryDiv());
        return
    }

    var url = "/entry/" + id;

    $.ajax({
        url: url,
        type: "get",
        contentType: "application/json"
    }).success(function (data) {
        entryDiv.replaceWith(showEntry(data));
    });
}

function showEntries(entries) {

    // reset
    $('#soundtrack').data('keysToDivs', {});

    var entriesDiv = $("<div class='entries'></div>");
    entriesDiv.sortable({
        update: updateEntryPosition
    });

    entriesDiv.append(createAddEntryDiv());

    $.each(entries, function() {
        var entryDiv = showEntry(this);
        $('#soundtrack').data('keysToDivs')[this.key] = entryDiv;
        entriesDiv.append(entryDiv);
    });

    return entriesDiv;
}

function updateEntryPosition( event, ui ) {

    var entryDiv = $(ui.item);
    var entryYear = Number(entryDiv.find('div.year').text());
    var entryOrdinal = Number(entryDiv.find('div.ordinal').text());
    var prevEntryDiv = entryDiv.prev();
    var prevEntryYear = Number(prevEntryDiv.find('div.year').text());
    var prevEntryOrdinal = Number(prevEntryDiv.find('div.ordinal').text());

    // add one if moving from a different year or from later in the same year
    if (entryYear != prevEntryYear || entryOrdinal > prevEntryOrdinal) {
        prevEntryOrdinal += 1;
    }

    // TODO: factor all this out into a helper method
    var id = entryDiv.attr('id');
    var url = "/entry/" + id;
    var entry = {
        year: prevEntryYear,
        ordinal: prevEntryOrdinal
    };

    $.ajax({
        url: url,
        type: "patch",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(entry)
    }).success(function (data) {
        var newEntryDiv = showEntry(data);
        entryDiv.fadeOut(500, function () {
            entryDiv.replaceWith(newEntryDiv);
            refreshEntries();
        });
    });
}

function createAddEntryDiv() {

    var titleDiv = $("<div class='title'><div class='add-entry title-button'>add a new entry</div>");
    var entryDiv = $("<div class='entry'/>");
    entryDiv.append(titleDiv);
    entryDiv.append("<hr/>");

    return entryDiv;
}

function showEntry(entry) {

    var titleDiv = $("<div class='title'><div class='year read-year title-button' id='" + entry.year + "'>" + entry.year + "</div><div class='ordinal title-button'>" + (entry.ordinal < 10 ? "0" : "") + entry.ordinal + "</div> " + entry.title + " -- " + entry.artist + " <div class='edit-entry title-button'>edit</div></div>");
    var storyDiv = $("<div class='story'>" + storyify(entry.story) + "</div>");
    var entryDiv = $("<div class='entry' id='" + entry.key + "'/>");
    entryDiv.append(titleDiv);
    entryDiv.append(storyDiv);
    entryDiv.append("<hr/>");

    return entryDiv;
}

function showEntryForEdit(entry) {

    var overlayDiv = $("<div class='overlay no-op'/>");
    var contentDiv = $("<div class='overlay-content'/>");
    overlayDiv.append(contentDiv);

    var headerDiv = $("<div class='edit-entry-top'/>");
    var yearDiv = $("<label for='year' class='year'><span>year</span><input type='text' id='year' value='" + entry.year + "'/></label>");
    var ordinalDiv = $("<label for='ordinal' class='ordinal'><span>ordinal</span><input type='text' id='ordinal' value='" + entry.ordinal + "'/></label>");
    var titleDiv = $("<label for='title' class='title'><span>title</span><input type='text' id='title' value='" + valuify(entry.title) + "'/></label>");
    var artistDiv = $("<label for='artist' class='artist'><span>artist</span><input type='text' id='artist' value='" + valuify(entry.artist) + "'/></label>");
    headerDiv.append(yearDiv);
    headerDiv.append(ordinalDiv);
    headerDiv.append(titleDiv);
    headerDiv.append(artistDiv);
    contentDiv.append(headerDiv);

    var storyDiv= $("<div class='edit-entry-bottom'><textarea class='no-op edit-story'>" + textareaify(entry.story) + "</textarea></div>");
    contentDiv.append(storyDiv);

    var buttonsDiv = $("<div class='close-buttons'/>");
    var saveButton = $("<div class='close-button save-entry'>save</div>");
    var cancelButton = $("<div class='close-button read-entry'>cancel</div>");
    buttonsDiv.append(saveButton);
    buttonsDiv.append(cancelButton);
    storyDiv.append(buttonsDiv);

    return overlayDiv;
}

function readNameTag($$) {

    var id = $$.attr('id');
    var url = "/entries?personTags=" + id;

    $.ajax({
        url: url,
        type: "get",
        contentType: "application/json"
    }).success(function (data) {
        $("#soundtrack").html(showEntries(data));
    });
}

// TODO: this is obv pretty similar to readNameTag... maybe consolidate
function readHashTag($$) {

    var id = $$.attr('id');
    var url = "/entries?hashTags=" + id;

    $.ajax({
        url: url,
        type: "get",
        contentType: "application/json"
    }).success(function (data) {
        $("#soundtrack").html(showEntries(data));
    });
}

function readYear($$) {

    var id = $$.attr('id');
    var url = "/entries/" + id;

    $.ajax({
        url: url,
        type: "get",
        contentType: "application/json"
    }).success(function (data) {
        $("#soundtrack").html(showEntries(data));
    });
}

function storyify(text) {

    text = text.replace(/(\@|\#)([0-9a-z\-]*)(\{(.*?)\})?/g, nameify);
    text = text.replace(/(?:\r\n|\r|\n)/g, "<br/>");

    return text;
}

function nameify(match, p1, p2, p3, p4, offset, text) {
    var divClass = p1 == '@' ? 'name-tag' : 'hash-tag';
    return "<div class='" + divClass + "' id='" + p2 + "'>" + (!p4 ? p1 + p2 : p4) + "</div>";
}

function valuify(text) {
    return null == text ? "" : text.replace(/\'/g, "&apos;");
}

function textareaify(text) {
    return null == text ? "" : text;
}