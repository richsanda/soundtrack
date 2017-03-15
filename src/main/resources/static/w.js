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
        $('#soundtrack').html(showEntries(data, updateEntryPosition, createAddEntryDiv));
    });
}

function refreshFavorites() {

    var url = "/rankedList?type=FAVORITE";

    $.ajax({
        url: url,
        dataType: "json"
    }).success(function (data) {
        $('#soundtrack').html(showEntries(data.entries, function() {}, createSaveFavoritesDiv));
    });
}

function refreshTags() {

    var url = "/hashTags?sort=FULL";

    $.ajax({
        url: url,
        dataType: "json"
    }).success(function (data) {
        $('#soundtrack').html(showTags(data));
    });
}

function refreshPersons() {

    var url = "/persons";

    $.ajax({
        url: url,
        dataType: "json"
    }).success(function (data) {
        $('#soundtrack').html(showPersons(data));
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
        'delete-entry' : function ($$) {
            deleteEntry($$);
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
        'show-soundtrack' : function ($$) {
            refreshEntries();
        },
        'show-tags' : function ($$) {
            refreshTags();
        },
        'show-persons' : function ($$) {
            refreshPersons();
        },
        'show-favorites' : function ($$) {
            refreshFavorites();
        },
        'save-favorites' : function ($$) {
            updateFavorites();
        },
        'add-tag' : function ($$) {
            addTag($$);
        },
        'edit-tag' : function ($$) {
            editTag($$);
        },
        'save-tag' : function ($$) {
            saveTag($$);
        },
        'read-tag' : function ($$) {
            readTag($$);
        },
        'delete-tag' : function ($$) {
            deleteTag($$);
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
        "year" : null,
        "ordinal" : null,
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

function editTag($$) {

    var tagDiv = $$.closest('div.tag');
    var id = tagDiv.attr('id');
    var url = "/hashTag/" + id;

    $.ajax({
        url: url,
        dataType: "json"
    }).success(function (data) {
        var overlayDiv = showTagForEdit(data);
        overlayDiv.width("100%");
        overlayDiv.fadeIn(200);
        tagDiv.append(overlayDiv);
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
                //refreshEntries();
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

function saveTag($$) {

    var tagDiv = $$.closest('div.tag');
    var id = tagDiv.attr('id');
    var tag = {
        story: tagDiv.find('textarea.edit-story').val(),
        fullTag: tagDiv.find('#full').val(),
        name: tagDiv.find('#name').val()
    };

    $.ajax({
        url: "/hashTag/" + id,
        type: "patch",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(tag)
    }).success(function (data) {
        var newTagDiv = showTag(data);
        tagDiv.fadeOut(500, function () {
            tagDiv.replaceWith(newTagDiv);
        });
    });
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

function readTag($$) {

    var tagDiv = $$.closest('div.tag');
    var id = tagDiv.attr('id');

    var url = "/hashTag/" + id;

    $.ajax({
        url: url,
        type: "get",
        contentType: "application/json"
    }).success(function (data) {
        tagDiv.replaceWith(showTag(data));
    });
}

function deleteEntry($$) {

    var entryDiv = $$.closest('div.entry');
    var id = entryDiv.attr('id');

    var url = "/entry/" + id;

    $.ajax({
        url: url,
        type: "delete",
        contentType: "application/json"
    }).success(function (data) {
        // var newEntryDiv = showEntry(data);
        entryDiv.fadeOut(500, function () {
            entryDiv.remove();
            refreshEntries();
        });
    });
}

function deleteTag($$) {

    var tagDiv = $$.closest('div.tag');
    var id = tagDiv.attr('id');

    var url = "/hashTag/" + id;

    $.ajax({
        url: url,
        type: "delete",
        contentType: "application/json"
    }).success(function (data) {
        // var newEntryDiv = showEntry(data);
        tagDiv.fadeOut(500, function () {
            tagDiv.remove();
            refreshTags();
        });
    }).error(function (data) {
        alert("can't delete !")
    });
}

function showEntries(entries, updateFunction, navFunction) {

    // reset
    $('#soundtrack').data('keysToDivs', {});

    var entriesDiv = $("<div class='entries'></div>");

    if (updateFunction) {
        entriesDiv.sortable({
            update: updateFunction
        });
    }

    if (navFunction) {
        entriesDiv.append(navFunction);
    }

    $.each(entries, function() {
        var entryDiv = showEntry(this);
        $('#soundtrack').data('keysToDivs')[this.key] = entryDiv;
        entriesDiv.append(entryDiv);
    });

    return entriesDiv;
}

function showTags(tags) {

    // reset
    $('#tags').data('keysToDivs', {});

    var tagsDiv = $("<div class='tags'></div>");

    $.each(tags, function() {
        var tagDiv = showTag(this);
        $('#tags').data('keysToDivs')[this.key] = tagDiv;
        tagsDiv.append(tagDiv);
    });

    return tagsDiv;
}

function showPersons(persons) {

    // reset
    $('#persons').data('keysToDivs', {});

    var personsDiv = $("<div class='persons'></div>");

    $.each(persons, function() {
        var personDiv = showPerson(this);
        $('#persons').data('keysToDivs')[this.key] = personDiv;
        personsDiv.append(personDiv);
    });

    return personsDiv;
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

function updateFavoritePosition( event, ui ) {

    var entryDiv = $(ui.item);
    var entriesDiv = entryDiv.closest('div.entries');
    var entryDivs = entriesDiv.find('div.entry');
    var entryKeys = [];
    entryDivs.each(function(e) { entryKeys.push($(e).attr('id'))});

    var json = {
        type : "FAVORITE",
        entries : entryKeys
    };

    var url = "/rankedList?type=FAVORITE";

    $.ajax({
        url: url,
        type: "patch",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(json)
    }).success(function (data) {
        var newEntryDiv = showEntry(data);
        entryDiv.fadeOut(500, function () {
            refreshFavorites();
        });
    });
}

function updateFavorites() {

    var entryDivs = $('#soundtrack').find('div.entry');
    var entryKeys = [];
    entryDivs.each(function() {
        var id = $(this).attr('id');
        if (id) entryKeys.push(id);
    });

    var json = {
        type : "FAVORITE",
        entries : entryKeys
    };

    var url = "/rankedList?type=FAVORITE";

    $.ajax({
        url: url,
        type: "patch",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(json)
    }).success(function (data) {
        $('#soundtrack').children('div.entries').fadeOut(200, function () {
            $('#soundtrack').html(showEntries(data.entries, function() {}, createSaveFavoritesDiv));
        });
    });
}

function createAddEntryDiv() {

    var titleDiv = $("<div class='title'><div class='add-entry title-button nav-button'>add a new entry</div>");
    var entryDiv = $("<div class='entry'/>");
    entryDiv.append(titleDiv);
    entryDiv.append("<hr/>");

    return entryDiv;
}

function createSaveFavoritesDiv() {

    var titleDiv = $("<div class='title'><div class='save-favorites title-button nav-button'>save favorites</div>");
    var entryDiv = $("<div class='entry'/>");
    entryDiv.append(titleDiv);
    entryDiv.append("<hr/>");

    return entryDiv;
}

function showEntry(entry) {

    var titleDiv = $("<div class='title'><div class='year read-year title-button' id='" + entry.year + "'>" + entry.year + "</div><div class='ordinal title-button'>" + (entry.ordinal < 10 ? "0" : "") + entry.ordinal + "</div> " + entry.title + " -- " + entry.artist + " <div class='edit-entry title-button'>edit</div><div class='delete-entry title-button'>delete</div></div>");
    var storyDiv = $("<div class='story'>" + storyify(entry.story) + "</div>");
    var entryDiv = $("<div class='entry' id='" + entry.key + "'/>");
    entryDiv.append(titleDiv);
    entryDiv.append(storyDiv);
    entryDiv.append("<hr/>");

    return entryDiv;
}

function showTag(tag) {

    var titleDiv = $("<div class='title'><div class='tag-title title-button' id='" + tag.tag + "'>" + tag.tag + " (" + tag.appearances + ")" + "</div> " + valuify(tag.fullTag) + " <div class='edit-tag title-button'>edit</div><div class='delete-tag title-button'>delete</div></div>");
    var storyDiv = $("<div class='story'>" + storyify(tag.story) + "</div>");
    var tagDiv = $("<div class='tag' id='" + tag.key + "'/>");
    tagDiv.append(titleDiv);
    tagDiv.append(storyDiv);
    tagDiv.append("<hr/>");

    return tagDiv;
}

function showPerson(person) {

    var titleDiv = $("<div class='title'><div class='person-title title-button' id='" + person.tag + "'> " + person.tag + " (" + person.appearances + ")" + "</div> " + valuify(person.name) + " <div class='edit-person title-button'>edit</div><div class='delete-person title-button'>delete</div></div>");
    var storyDiv = $("<div class='story'>" + storyify(person.story) + "</div>");
    var personDiv = $("<div class='person' id='" + person.key + "'/>");
    personDiv.append(titleDiv);
    personDiv.append(storyDiv);
    personDiv.append("<hr/>");

    return personDiv;
}

function showEntryForEdit(entry) {

    var overlayDiv = $("<div class='overlay no-op'/>");
    var contentDiv = $("<div class='overlay-content'/>");
    overlayDiv.append(contentDiv);

    var headerDiv = $("<div class='edit-entry-top'/>");
    var yearDiv = $("<label for='year' class='year'><span>year</span><input type='text' id='year' value='" + numerify(entry.year) + "'/></label>");
    var ordinalDiv = $("<label for='ordinal' class='ordinal'><span>ordinal</span><input type='text' id='ordinal' value='" + numerify(entry.ordinal) + "'/></label>");
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

function showTagForEdit(tag) {

    var overlayDiv = $("<div class='overlay no-op'/>");
    var contentDiv = $("<div class='overlay-content'/>");
    overlayDiv.append(contentDiv);

    var headerDiv = $("<div class='edit-entry-top'/>");
    var tagDiv = $("<div><label for='tag' class='tag'><span>" + valuify(tag.tag) + " (" + tag.appearances + ")</span></label></div>");
    var fullDiv = $("<label for='full' class='full'><span>full</span><input type='text' id='full' value='" + valuify(tag.fullTag) + "'/></label>");
    var nameDiv = $("<label for='name' class='name'><span>name</span><input type='text' id='name' value='" + valuify(tag.name) + "'/></label>");
    headerDiv.append(tagDiv);
    headerDiv.append(fullDiv);
    headerDiv.append(nameDiv);
    contentDiv.append(headerDiv);

    var storyDiv= $("<div class='edit-entry-bottom'><textarea class='no-op edit-story'>" + textareaify(tag.story) + "</textarea></div>");
    contentDiv.append(storyDiv);

    var buttonsDiv = $("<div class='close-buttons'/>");
    var saveButton = $("<div class='close-button save-tag'>save</div>");
    var cancelButton = $("<div class='close-button read-tag'>cancel</div>");
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

    if (!text) return "";

    text = text.replace(/(@|#)([0-9a-z\-\./]*)(\{(.*?)\})?/g, nameify);
    text = text.replace(/(?:\r\n|\r|\n)/g, "<br/>");

    return text;
}

function nameify(match, p1, p2, p3, p4, offset, text) {

    var divClass = p1 == '@' ? 'name-tag' : 'hash-tag';
    var linkText = p4;
    var tagText = p2;
    var tagSepIndex = p2.indexOf('/');
    var tagHasSep = tagSepIndex > 0;

    if (tagHasSep) {
        divClass += ' ' + p2.substring(0, tagSepIndex);
        tagText = p2.substring(tagSepIndex + 1);
    } else {
        divClass += ' general';
    }
    tagText = tagText.replace(/\./g, ' ');
    if (!linkText) linkText = tagText;

    return "<div class='" + divClass + "' id='" + p2 + "'>" + linkText + "</div>";
}

function valuify(text) {
    return null == text ? "" : text.replace(/\'/g, "&apos;");
}

function numerify(text) {
    return null == text ? "" : text;
}

function textareaify(text) {
    return null == text ? "" : text;
}