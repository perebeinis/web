function CommentsComponent(){
}

CommentsComponent.prototype.comments = function(data, parentElementId, elementValue) {
    var scope = this;
    this.commentsMainElemId = parentElementId;
    this.commentsMainElemData = data;
    if(Utils.checkFieldCanDisplay(data)){
        $('#' + parentElementId)
            .append($('<div>', {class: "comments" + " " + data[this.customClassName]})
                .append($('<span>', {class: "hidden popup"})
                    .html(this.messages["mandatory"]))
                .append($('<button>', {class: "btn btn-primary btn-md"}).click(function () {
                    scope.createCommentModalWindow("root");
                }).html(scope.messages["constants.header.addComment"]))).append($('<div>', {class: "comments-list"}));

        if(elementValue) {
            this.createCommentsList(elementValue[0].child, $('#' + parentElementId).find('div.comments-list')[0]);
        }

        if (data[this.mandatoryCondition] != "" && data[this.mandatoryCondition] != "none") {
            this.mandatoryCondtitions[data[this.name]] = data[this.mandatoryCondition];
        }
    }

}

CommentsComponent.prototype.createCommentsList = function (commentsArr, parentElement){
    var scope = this;
    for (var i in commentsArr) {
        var comment = commentsArr[i];
        var subCommentsList = $('<div>', {class : "comments-list"});
        $(parentElement)
            .append($('<div>', {class : "comment-data"})
                .append($('<div>', {class : "comment"})
                    .append($('<div>', {class : "comment-block", id : comment.comment_id["$oid"]})
                        .append($('<span>', {class : "creator"}).html(comment.creator))

                        .append($('<input>', {type : "image", alt: "submit", src: "images/comment.jpg"})
                            .click((function () {
                                scope.createCommentModalWindow(this.parentNode.id);
                            })))

                        .append($('<span>', {class : "value"}).html(comment.commentValue)))
                    .append($('<span>', {class : "created"}).html(new Date(comment.created["$date"]).toLocaleString()))))
            .append(subCommentsList);

        this.createCommentsList(comment.child, subCommentsList);
    }
}

CommentsComponent.prototype.createCommentModalWindow = function(parentId){
    var parentId = parentId;
    var scope = this;
    var modalWindowId = "dynamicModal";
    $('body').append(
        $('<div>', {id : "dynamicModal", class : "modal fade", tabindex : "-1" , role : "dialog", "aria-labelledby" : "confirm-modal", "aria-hidden" : "true"})
            .append($('<div>', {class : "modal-dialog"})
                .append($('<div>', {class : "modal-content"})
                    .append($('<div>', {class : "modal-header"})
                        .append($('<a>', {class : "close", "data-dismiss" : "modal"}).html("×"))
                        .append($('<h4>').html(scope.messages["constants.header.newComment"])))
                    .append($('<div>', {class : "modal-body"})
                        .append($('<textarea>', {class: "form-control", rows : 6})))
                    .append($('<div>', {class : "modal-footer"})
                        .append($('<span>', {class : "btn btn-primary", "data-dismiss" : "modal"}).html(scope.messages["constants.save"]).click(function(){
                            scope.postComment($("#"+modalWindowId).find('textarea')[0].value, parentId);
                        }))
                        .append($('<span>', {class : "btn btn-primary", "data-dismiss" : "modal"}).html(scope.messages["constants.close"]))
                    )
                )
            )
    );

    $("#"+modalWindowId).modal();
    $("#"+modalWindowId).modal('show');
    $("#"+modalWindowId).on('hidden.bs.modal', function (e) {
        $(this).remove();
    });
}

CommentsComponent.prototype.postComment = function(commentValue, parentElementId){
    var scope = this;
    var postData = new Object();
    postData["commentValue"] = commentValue;
    postData["documentId"] = scope.getSearchParams("id");
    postData["parent"] = parentElementId;

    $.ajax({
        url: '/create-new-comment?type='+scope.getSearchParams("type"),
        type: "POST",
        contentType: "application/json",
        data: encodeURI(JSON.stringify(postData)),
        dataType: 'json'
    }).done(function (data) {
        $("#"+scope.commentsMainElemId).html("");
        scope.comments(scope.commentsMainElemData, scope.commentsMainElemId, data);
    });
}