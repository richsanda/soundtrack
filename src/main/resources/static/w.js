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
        'start-game' : function ($$) {
            startGame($$);
        }
    };

    regionClick(e, actions);
}

function actionsBehavior(root) {

    $(root).on('click touchstart', actionsClick);

    return root
}

function showEntries(entries) {

    var entriesDiv = $("<div class='entries'></div>");

    $.each(entries, function() {
        var entryDiv = $("<div class='entry'>" + this.title + "</div>");
        entriesDiv.append(entryDiv);
    });

    return entriesDiv;
}