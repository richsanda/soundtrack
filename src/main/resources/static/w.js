$(pageBehavior);

var edit = false;

function pageBehavior () {

    actionsBehavior($(this));
    showIntroPane();
}

function refreshEntries(showStory) {

    var url = "/soundtrack";

    $.ajax({
        url: url,
        dataType: "json"
    }).success(function (data) {
        showPane(buildAllPane("all", true), buildEntries(data, updateEntryPosition, createAddEntryDiv, showStory));
    });
}

function refreshOverall() {

    var url = "/soundtrack/ranked";

    $.ajax({
        url: url,
        dataType: "json"
    }).success(function (data) {
        showPane(buildOverallPane("top 100"), buildEntries(data));
    });
}

function randomizeFavorites() {

    var url = "/soundtrack/random";

    $.ajax({
        url: url,
        dataType: "json"
    }).success(function (data) {
        $('#soundtrack').html(buildEntries(data, function() {}, createSaveFavoritesDiv));
    });
}

function seedWithFavorites(createDivFunction) {

    refreshRankedList("FAVORITE", createDivFunction);
}

function seedWithRepresentatives(createDivFunction) {

    refreshRankedList("REPRESENTATIVE", createDivFunction);
}

function refreshFavorites() {

    refreshRankedList("FAVORITE", createSaveFavoritesDiv);
}

function refreshRepresentatives() {

    refreshRankedList("REPRESENTATIVE", createSaveRepresentativesDiv);
}

function refreshShared() {

    refreshRankedList("SHARED", createSaveSharedDiv);
}

function refreshRankedList(type, saveDivFunction) {

    var url = "/rankedList?type=" + type;

    $.ajax({
        url: url,
        dataType: "json"
    }).success(function (data) {
        $('#soundtrack').html(buildEntries(data.entries, function() {}, saveDivFunction));
    });
}

function refreshTags() {

    var url = "/hashTags?sort=FULL";

    $.ajax({
        url: url,
        dataType: "json"
    }).success(function (data) {
        showPane(buildTags(data));
    });
}

function showPane(paneDiv, soundtrackDiv) {
    $('#pane').html(paneDiv);
    if (typeof soundtrackDiv == typeof undefined || !soundtrackDiv) {
        $('#soundtrack').empty();
    } else {
        $('#soundtrack').html(soundtrackDiv);
    }
    $('#intro').hide();
    $('#years').hide();
    $('#pane').show();
}

function refreshPersons() {

    var url = "/persons";

    $.ajax({
        url: url,
        dataType: "json"
    }).success(function (data) {
        showPane(buildPersons(data));
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
            refreshEntries(true);
            clearAndUpdateSoundtrackDiv();
        },
        'show-intro' : function ($$) {
            showIntroPane();
        },
        'show-years' : function ($$) {
            showYearsPane();
        },
        'show-artists' : function ($$) {
            // showArtistsPane();
        },
        'show-tags' : function ($$) {
            refreshTags();
        },
        'show-persons' : function ($$) {
            refreshPersons();
        },
        'show-favorites' : function ($$) {
            refreshFavorites();
            clearAndUpdateSoundtrackDiv('favorites');
        },
        'save-favorites' : function ($$) {
            updateFavorites();
            clearAndUpdateSoundtrackDiv('favorites');
        },
        'show-representatives' : function ($$) {
            refreshRepresentatives();
            clearAndUpdateSoundtrackDiv('representatives');
        },
        'save-representatives' : function ($$) {
            updateRepresentatives();
            clearAndUpdateSoundtrackDiv('representatives');
        },
        'show-shared' : function ($$) {
            refreshShared();
            clearAndUpdateSoundtrackDiv('shared');
        },
        'save-shared' : function ($$) {
            updateShared();
            clearAndUpdateSoundtrackDiv('shared');
        },
        'show-random' : function ($$) {
            randomizeFavorites();
        },
        'show-overall' : function ($$) {
            refreshOverall();
        },
        'toggle-story' : function ($$) {
            var entryDiv = $$.closest('div.entry');
            var storyDiv = entryDiv.find('div.story');
            storyDiv.toggle();
        },
        'show-stories' : function ($$) {
            $('#soundtrack').find('div.story').show();
            $$.text('hide stories');
            $$.removeClass('show-stories').addClass('hide-stories');
        },
        'hide-stories' : function ($$) {
            $('#soundtrack').find('div.story').hide();
            $$.text('show stories');
            $$.removeClass('hide-stories').addClass('show-stories');
        },
        'move-to' : function ($$) {
            var entryDiv = $$.closest('div.entry');
            var idx = entryDiv.find('#moveTo').val();
            entryDiv.fadeOut(250, function() {
                entryDiv.insertAfter($('#soundtrack div.entry:eq(' + idx + ')'));
                entryDiv.fadeIn(250);
            });
        },
        'seed-representatives-with-favorites' : function ($$) {
            seedWithFavorites(createSaveRepresentativesDiv);
        },
        'seed-favorites-with-representatives' : function ($$) {
            seedWithRepresentatives(createSaveFavoritesDiv);
        },
        'seed-shared-with-favorites' : function ($$) {
            seedWithFavorites(createSaveSharedDiv);
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
            "youtube" : null,
            "releaseDate" : null
        }

    var entryDiv = $$.closest('div.entry');
    var overlayDiv = buildEntryForEdit(data);
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
        var overlayDiv = buildEntryForEdit(data);
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
        artist: entryDiv.find('#artist').val(),
        releaseDate: dateify(entryDiv.find('#release').val())
    };

    if (null != id) {
        $.ajax({
            url: "/entry/" + id,
            type: "patch",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(entry)
        }).success(function (data) {
            var newEntryDiv = showEntry(data, true);
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
            var newEntryDiv = showEntry(data, true);
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
        entryDiv.replaceWith(showEntry(data, true));
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
        entryDiv.fadeOut(500, function () {
            entryDiv.remove();
            refreshEntries(true);
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
        tagDiv.fadeOut(500, function () {
            tagDiv.remove();
            refreshTags();
        });
    }).error(function (data) {
        alert("can't delete !")
    });
}

function buildEntries(entries, updateFunction, navFunction, showStory) {

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

    var i = 1;
    $.each(entries, function() {
        var entryDiv = showEntry(this, showStory, i++);
        $('#soundtrack').data('keysToDivs')[this.key] = entryDiv;
        entriesDiv.append(entryDiv);
    });

    return entriesDiv;
}

function buildTags(tags) {

    // reset
    $('#tags').data('keysToDivs', {});

    var tagsDiv = $("<div class='tags'></div>");

    $.each(tags, function() {
        var tagDiv = buildTagLink(this);
        $('#tags').data('keysToDivs')[this.key] = tagDiv;
        tagsDiv.append(tagDiv);
    });

    return tagsDiv;
}

function buildPersons(persons) {

    // reset
    $('#persons').data('keysToDivs', {});

    var personsDiv = $("<div class='people'></div>");

    $.each(persons, function() {
        var personDiv = buildPersonLink(this);
        $('#persons').data('keysToDivs')[this.key] = personDiv;
        personsDiv.append(personDiv);
    });

    return personsDiv;
}

function updateEntryPosition( event, ui ) {

    var showStory = true;
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
        var newEntryDiv = showEntry(data, showStory);
        entryDiv.fadeOut(500, function () {
            entryDiv.replaceWith(newEntryDiv);
            refreshEntries(showStory);
        });
    });
}

function updateFavorites() {
    updateRankedList("FAVORITE", createSaveFavoritesDiv);
}

function updateRepresentatives() {
    updateRankedList("REPRESENTATIVE", createSaveRepresentativesDiv);
}

function updateShared() {
    updateRankedList("SHARED", createSaveSharedDiv);
}

function updateRankedList(type, createDivFunction) {

    var entryDivs = $('#soundtrack').find('div.entry');
    var entryKeys = [];
    entryDivs.each(function() {
        var id = $(this).attr('id');
        if (id) entryKeys.push(id);
    });

    var json = {
        type : type,
        entries : entryKeys
    };

    var url = "/rankedList?type=" + type;

    $.ajax({
        url: url,
        type: "patch",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(json)
    }).success(function (data) {
        $('#soundtrack').children('div.entries').fadeOut(200, function () {
            $('#soundtrack').html(buildEntries(data.entries, function() {}, createDivFunction));
        });
    });
}

function createAddEntryDiv() {

    if (edit) {

        var titleDiv = $("<div class='title'><div class='add-entry title-button nav-button'>add a new entry</div>");
        var entryDiv = $("<div class='entry'/>");
        entryDiv.append(titleDiv);
        entryDiv.append("<hr/>");

        return entryDiv;

    } else {
        return null;
    }
}

function createSaveFavoritesDiv() {

    var titleDiv = $("<div class='title'><div class='save-favorites title-button nav-button'>save favorites</div><div class='show-random title-button nav-button'>randomize</div><div class='seed-favorites-with-representatives title-button nav-button'>seed reps</div></div>");
    var entryDiv = $("<div class='entry'/>");
    entryDiv.append(titleDiv);
    entryDiv.append("<hr/>");

    return entryDiv;
}

function createSaveRepresentativesDiv() {

    var titleDiv = $("<div class='title'><div class='save-representatives title-button nav-button'>save representatives</div><div class='seed-representatives-with-favorites title-button nav-button'>seed favorites</div></div>");
    var entryDiv = $("<div class='entry'/>");
    entryDiv.append(titleDiv);
    entryDiv.append("<hr/>");

    return entryDiv;
}

function createSaveSharedDiv() {

    var titleDiv = $("<div class='title'><div class='save-shared title-button nav-button'>save shared</div><div class='seed-shared-with-favorites title-button nav-button'>seed favorites</div></div>");
    var entryDiv = $("<div class='entry'/>");
    entryDiv.append(titleDiv);
    entryDiv.append("<hr/>");

    return entryDiv;
}

function showIntroPane() {

    $('#pane').hide();
    $('#years').hide();
    $('#intro').show();
    $('#soundtrack').empty();
}

function showYearsPane() {

    $('#pane').hide();
    $('#intro').hide();
    $('#years').show();
    $('#soundtrack').empty();
}

function buildAllPane(title, showStories) {

    var soundtrackPaneDiv = $(
        "<div class='soundtrack-pane'>" +
        "<div class='soundtrack-title'>" + title + "</div>" +
        "</div>"
    );

    soundtrackPaneDiv.append(buildStoryButtonDiv(showStories));

    return soundtrackPaneDiv;
}

function buildYearPane(title, showStories) {

    var yearPaneDiv = $(
        "<div class='soundtrack-pane'>" +
        "<div class='soundtrack-title'>" + title + "</div>" +
        "</div>"
    );

    yearPaneDiv.append(buildStoryButtonDiv(showStories));

    return yearPaneDiv;
}

function buildPeoplePane(title, showStories) {

    var peoplePaneDiv = $(
        "<div class='soundtrack-pane'>" +
        "<div class='soundtrack-title'>" + title + "</div>" +
        "</div>"
    );

    peoplePaneDiv.append(buildStoryButtonDiv(showStories));

    return peoplePaneDiv;
}

function buildTagPane(title, showStories) {

    var tagPaneDiv = $(
        "<div class='soundtrack-pane'>" +
        "<div class='soundtrack-title'>" + title + "</div>" +
        "</div>"
    );

    tagPaneDiv.append(buildStoryButtonDiv(showStories));

    return tagPaneDiv;
}

function buildOverallPane(title, showStories) {

    var overallPaneDiv = $(
        "<div class='soundtrack-pane'>" +
        "<div class='soundtrack-title'>" + title + "</div>" +
        "</div>"
    );

    overallPaneDiv.append(buildStoryButtonDiv(showStories));

    return overallPaneDiv;
}

function buildStoryButtonDiv(showStories) {
    if (showStories) {
        return $("<div class='tab hide-stories'>hide stories</div>");
    } else {
        return $("<div class='tab show-stories'>show stories</div>");
    }
}

function showEntry(entry, showStory, count) {

    var timelineDiv = showTimeline();

    var timespanTitle = "released " + releaseify(entry.releaseDate) + "; entry " + entry.ordinal + " of 20 in " + entry.year;

    var releaseCircleDiv = showCircle(entry.releasePosition, 5, timespanTitle);
    var soundtrackCircleDiv = showCircle(entry.soundtrackPosition, Math.max(5, Math.sqrt(entry.score) * 1.5), timespanTitle);
    var timespanDiv = $("<div class='timespan' title='" + timespanTitle + "'></div>");
    timespanDiv.css({
        'left': entry.releasePosition + '%',
        'right': 100 - entry.soundtrackPosition + '%'
    });

    timelineDiv.append(releaseCircleDiv);
    timelineDiv.append(soundtrackCircleDiv);
    timelineDiv.append(timespanDiv);

    var titleDiv = $("<div class='title toggle-story'/>");

    var yearOrdinalDiv = $(
        "<div class='year-ordinal'>" +
        "<div class='count'>" + count + ". </div>" +
        "<div class='year read-year title-button' id='" + entry.year + "'>" + entry.year + "</div>" +
        "<div class='ordinal'>" + (entry.ordinal < 10 ? "0" : "") + entry.ordinal + "</div>" +
        "</div>"
    );

    var titleInfoDiv = $(
        "<div class='title-info'>" +
        "<span class='title'>" + entry.title + " -- " + entry.artist + "</span>" +
        "</div>"
    );

    if (edit) {
        var editDiv = $("<div class='edit-entry edit-button title-button'>edit</div>");
        var deleteDiv = $("<div class='delete-entry edit-button title-button'>delete</div>");
        titleInfoDiv.append(editDiv);
        titleInfoDiv.append(deleteDiv);
    }

    titleDiv.css("background-color", calculateColor(entry));

    titleDiv.append(yearOrdinalDiv);
    titleDiv.append(timelineDiv);
    titleDiv.append(titleInfoDiv);

    var entryDiv = $("<div class='entry' id='" + entry.key + "'/>");
    entryDiv.append(titleDiv);

    var storyDiv = $("<div class='story'>" + storyify(entry.story) + "</div>");
    entryDiv.append(storyDiv);
    storyDiv.append("<hr/>");

    if (!showStory) {
        $(storyDiv).hide();
    }

    if (edit) {

        // proxy for sortable...
        var moveToDiv = $("<div class='title-button edit-button move-to'>move to</div><input type='text' size='4' id='moveTo' value='" + count + "'/>");
        titleInfoDiv.append(moveToDiv);
    }

    return entryDiv;
}

function showTimeline() {

    return $(
        "<div class='timeline-container'>" +
        "<div class='timeline-segment'>62</div>" +
        "<div class='timeline-segment'>67</div>" +
        "<div class='timeline-segment'>72</div>" +
        "<div class='timeline-segment'>77</div>" +
        "<div class='timeline-segment'>82</div>" +
        "<div class='timeline-segment'>87</div>" +
        "<div class='timeline-segment soundtrack-segment'>92</div>" +
        "<div class='timeline-segment soundtrack-segment'>97</div>" +
        "<div class='timeline-segment soundtrack-segment'>02</div>" +
        "<div class='timeline-segment soundtrack-segment'>07</div>" +
        "<div class='timeline-segment-last soundtrack-segment'>12</div>" +
        "</div>"
    );
}

function showCircle(p, d, t) {

    var circleDiv = $("<div class='soundtrack-circle' title='" + t + "'></div>");

    circleDiv.css({
        "left": "" + p + "%",
        'height': d + 'px',
        'width': d + 'px',
        'margin-left': '-' + d / 2 + 'px',
        'margin-top': '-' + d / 2 + 'px'
    });

    return circleDiv;
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

function buildTagLink(tag) {
    var tagLink = $(nameify(null, "#", tag.tag));
    tagLink.text(tagLink.text() + " (" + tag.appearances + ")");
    return tagLink;
}

function buildPerson(person) {

    var titleDiv = $("<div class='title'><div class='person-title title-button' id='" + person.tag + "'> " + person.tag + " (" + person.appearances + ")" + "</div> " + valuify(person.name) + " <div class='edit-person title-button'>edit</div><div class='delete-person title-button'>delete</div></div>");
    var storyDiv = $("<div class='story'>" + storyify(person.story) + "</div>");
    var personDiv = $("<div class='person' id='" + person.key + "'/>");
    personDiv.append(titleDiv);
    personDiv.append(storyDiv);
    storyDiv.append("<hr/>");

    return personDiv;
}

function buildPersonLink(person) {
    var personLink = $(nameify(null, "@", person.tag));
    //personLink.text(personLink.text() + " (" + person.appearances + ")");
    personLink.append(" (" + person.appearances + ")");
    return personLink;
}

function buildEntryForEdit(entry) {

    var overlayDiv = $("<div class='overlay no-op'/>");
    var contentDiv = $("<div class='overlay-content'/>");
    overlayDiv.append(contentDiv);

    var headerDiv = $("<div class='edit-entry-top'/>");
    var yearDiv = $("<label for='year' class='year'><span>year</span><input type='text' id='year' value='" + numerify(entry.year) + "'/></label>");
    var ordinalDiv = $("<label for='ordinal' class='ordinal'><span>ordinal</span><input type='text' id='ordinal' value='" + numerify(entry.ordinal) + "'/></label>");
    var titleDiv = $("<label for='title' class='title'><span>title</span><input type='text' id='title' value='" + valuify(entry.title) + "'/></label>");
    var artistDiv = $("<label for='artist' class='artist'><span>artist</span><input type='text' id='artist' value='" + valuify(entry.artist) + "'/></label>");
    var releaseDiv = $("<label for='release' class='release'><span>release</span><input type='text' id='release' value='" + valuify(entry.releaseDate) + "'/></label>");
    headerDiv.append(yearDiv);
    headerDiv.append(ordinalDiv);
    headerDiv.append(titleDiv);
    headerDiv.append(artistDiv);
    headerDiv.append(releaseDiv);
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
        showPane(buildPeoplePane(id), buildEntries(data, true));
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
        showPane(buildTagPane(id), buildEntries(data, true));
    });
}

function readYear($$) {

    var id = $$.attr('id');
    if (typeof id == typeof undefined || !id) id = "1992";
    var url = "/entries/" + id;

    $.ajax({
        url: url,
        type: "get",
        contentType: "application/json"
    }).success(function (data) {
        showPane(buildYearPane(id), buildEntries(data, true));
        $('#years').show();
    });
}

function clearAndUpdateSoundtrackDiv(newClass) {
    var classes = ['favorites', 'shared', 'representatives'];
    $.each(classes, function (i, c) {
        $('#soundtrack').removeClass(c);
    });
    if (classes.includes(newClass)) {
        $('#soundtrack').addClass(newClass);
    }
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

function parenthesize(text) {
    return null == text ? "" : " (" + text + ") ";
}

function dateify(text) {
    return (!text) ? null : text.length == 4 ? text + "-01-01" : text.length == 7 ? text + "-01" : text;
}

function releaseify(text) {

    if (!text) return null;
    var d = new Date(text);
    var months = ["jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"];
    var year = d.getFullYear();
    var month = d.getMonth();
    var day = d.getDay();
    return (day == 1 && month == 1) ? year : months[month - 1] + ", " + year;
}

function calculateColor(entry) {

    var r;
    var y;
    var b;
    var g;

    $.each(entry.rankings, function() {
            if (this.type == "FAVORITE") {
                r = Math.round(255 / 100 * this.score);
            } else if (this.type == "REPRESENTATIVE") {
                y = Math.round(255 / 100 * this.score);
            } else if (this.type = "SHARED") {
                b = Math.round(255 / 100 * this.score);
            }
            g = Math.round((b + y + y) / 3);
        }
    );

    return "rgba(" + r + ", " + g + ", " + b + ", " + entry.score / 600 + ")";
}