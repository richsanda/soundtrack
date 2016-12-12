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
        }
    };

    regionClick(e, actions);
}

function actionsBehavior(root) {

    $(root).on('click touchstart', actionsClick);

    return root
}

function editEntry($$) {

}

function showEntries(entries) {

    var entriesDiv = $("<div class='entries'></div>");

    $.each(entries, function() {
        var titleDiv = $("<div class='title'><div class='year'>" + this.year + "</div><div class='year'>" + (this.ordinal < 10 ? "0" : "") + this.ordinal + "</div> " + this.title + " -- " + this.artist + "</div>");
        var storyDiv = $("<div class='story'>" + this.story + "</div>");
        var entryDiv = $("<div class='entry' id='" + this.key + "'/>");
        entryDiv.append(titleDiv);
        entryDiv.append(storyDiv);
        entryDiv.append("<hr/>");
        entriesDiv.append(entryDiv);
    });

    return entriesDiv;
}