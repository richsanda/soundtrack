$(pageBehavior)

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
                behaviorMap[key]($$)
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
        }
    };

    regionClick(e, actions);
}

function actionsBehavior(root) {

    $(root).on('click touchstart', actionsClick);

    return root
}

function editEntry($$) {

    var id = $$.attr('id');
    var url = "/entry/" + id;

    $.ajax({
        url: url,
        dataType: "json"
    }).success(function (data) {
        var newDiv = showEntryForEdit(data);
        newDiv.css({width: 500, height: 100});
        $$.replaceWith(newDiv);
    });
}

function saveEntry($$) {

    var entryDiv = $$.closest('div.entry');
    var id = entryDiv.attr('id');
    var url = "/entry/" + id;
    var entry = {story: entryDiv.find('textarea.story').val()};

    $.ajax({
        url: url,
        type: "post",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(entry)
    }).success(function (data) {
        entryDiv.replaceWith(showEntry(data));
    });
}

function showEntries(entries) {

    var entriesDiv = $("<div class='entries'></div>");

    $.each(entries, function() {
        entriesDiv.append(showEntry(this));
    });

    return entriesDiv;
}

function showEntry(entry) {
    var titleDiv = $("<div class='title'><div class='year'>" + entry.year + "</div><div class='year'>" + (entry.ordinal < 10 ? "0" : "") + entry.ordinal + "</div> " + entry.title + " -- " + entry.artist + "</div>");
    var storyDiv = $("<div class='story'>" + entry.story + "</div>");
    var entryDiv = $("<div class='entry edit-entry' id='" + entry.key + "'/>");
    entryDiv.append(titleDiv);
    entryDiv.append(storyDiv);
    entryDiv.append("<hr/>");
    return entryDiv;
}

function showEntryForEdit(entry) {
    var titleDiv = $("<div class='title save-entry'><div class='year'>" + entry.year + "</div><div class='year'>" + (entry.ordinal < 10 ? "0" : "") + entry.ordinal + "</div> " + entry.title + " -- " + entry.artist + "</div>");
    var storyDiv = $("<textarea class='story'>" + entry.story + "</textarea>");
    var entryDiv = $("<div class='entry' id='" + entry.key + "'/>");
    entryDiv.append(titleDiv);
    entryDiv.append(storyDiv);
    entryDiv.append("<hr/>");
    return entryDiv;
}