$(pageBehavior);

function pageBehavior () {

    actionsBehavior($(this));

    var url = "/soundtrack";

    $.ajax({
        url: url,
        dataType: "json"
    }).success(function (data) {
        $('#soundtrack').append(showEntries(data));
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
    var url = "/entry/" + id;
    var entry = {
        story: entryDiv.find('textarea.edit-story').val(),
        year: entryDiv.find('#year').val(),
        ordinal: entryDiv.find('#ordinal').val()
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
        })
    });
}

function readEntry($$) {

    var entryDiv = $$.closest('div.entry');
    var id = entryDiv.attr('id');
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
    entriesDiv.sortable();

    $.each(entries, function() {
        var entryDiv = showEntry(this);
        entriesDiv.append(entryDiv);
    });

    return entriesDiv;
}

function showEntry(entry) {

    var titleDiv = $("<div class='title'><div class='year read-year' id='" + entry.year + "'>" + entry.year + "</div><div class='ordinal'>" + (entry.ordinal < 10 ? "0" : "") + entry.ordinal + "</div> " + entry.title + " -- " + entry.artist + " <div class='year edit-entry'>edit</div></div>");
    var storyDiv = $("<div class='story'>" + storyify(entry.story) + "</div>");
    var entryDiv = $("<div class='entry' id='" + entry.key + "'/>");
    entryDiv.append(titleDiv);
    entryDiv.append(storyDiv);
    entryDiv.append("<hr/>");

    $('#soundtrack').data('keysToDivs')[entry.key] = entryDiv;

    return entryDiv;
}

function showEntryForEdit(entry) {

    var overlayDiv = $("<div class='overlay no-op'/>");
    var contentDiv = $("<div class='overlay-content'/>");
    overlayDiv.append(contentDiv);

    var headerDiv = $("<div class='edit-entry-top'/>");
    var yearDiv = $("<label for='year' class='year'><span>year</span><input type='text' id='year' value='" + entry.year + "'/></label>");
    var ordinalDiv = $("<label for='ordinal' class='ordinal'><span>ordinal</span><input type='text' id='ordinal' value='" + entry.ordinal + "'/></label>");
    var titleDiv = $("<label for='title' class='title'><span>title</span><input type='text' id='title' value='" + entry.title.replace(/\'/g, "&apos;") + "'/></label>");
    var artistDiv = $("<label for='artist' class='artist'><span>artist</span><input type='text' id='artist' value='" + entry.artist.replace(/\`/g, "&apos;") + "'/></label>");
    headerDiv.append(yearDiv);
    headerDiv.append(ordinalDiv);
    headerDiv.append(titleDiv);
    headerDiv.append(artistDiv);
    contentDiv.append(headerDiv);

    var storyDiv= $("<div class='edit-entry-bottom'><textarea class='no-op edit-story'>" + entry.story + "</textarea></div>");
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

    text = text.replace(/(\@|\#)([0-9a-z\-]*)(\{(.*?)\})?/g, nameify2);
    text = text.replace(/(?:\r\n|\r|\n)/g, "<br/>");

    return text;
}

function nameify(tag, text) {
    return "<div class='name-tag' id='" + tag + "'>" + text + "</div>";
}

function nameify2(match, p1, p2, p3, p4, offset, text) {
    return "<div class='name-tag' id='" + p2 + "'>" + (!p4 ? '@' + p2 : p4) + "</div>";
}