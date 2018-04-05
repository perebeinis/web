function FormElements() {
    this.customClassName = "customClassName";
    this.title = "title";
    this.name = "name";
    this.typeForSaving = "typeForSaving";
    this.type = "type";

    return this;
}

FormElements.prototype.textField = function (data, parentElementId, elementValue) {
    $('#' + parentElementId)
        .append($('<div>', {class: "textField" + " " + data[this.customClassName]}).append($('<span>', {class: "hidden popup"}).html(this.messages["mandatory"])).append($('<label>', {value: data[this.title]}).html(this.messages[data[this.title]])).append($('<input>', {
            class: "form-control " + data[this.name],
            name: data[this.name],
            customType: data[this.typeForSaving] ? data[this.typeForSaving] : "",
            type: 'text',
            value: elementValue != null ? elementValue : "",
            readonly: !Utils.checkFieldEnabled(data)
        })));

    if (data[this.mandatoryCondition] != "") {
        this.mandatoryCondtitions[data[this.name]] = data[this.mandatoryCondition];
    }

}

FormElements.prototype.textArea = function (data, parentElementId, elementValue) {
    $('#' + parentElementId)
        .append($('<div>', {class: "form-group" + " " + data[this.customClassName]})
            .append($('<span>', {class: "hidden popup"}).html(this.messages["mandatory"]))
            .append($('<label>', {value: data[this.title]}).html(this.messages[data[this.title]]))
            .append($('<textarea>', {
                class: "form-control " + data[this.name],
                name: data[this.name],
                customType: data[this.typeForSaving] ? data[this.typeForSaving] : "",
                type: 'text',
                rows: "10",
                value: elementValue != null ? elementValue.replace(new RegExp('%', 'g'), '\n') : "",
                readonly: !Utils.checkFieldEnabled(data)
            }).text(elementValue != null ? elementValue.replace(new RegExp('%', 'g'), '\n') : "")));

    if (data[this.mandatoryCondition] != "") {
        this.mandatoryCondtitions[data[this.name]] = data[this.mandatoryCondition];
    }

}


FormElements.prototype.date = function (data, parentElementId, elementValue) {
    $('#' + parentElementId)
        .append($('<div>', {
            class: "input-group date" + " " + data[this.customClassName],
            id: "user-card-" + data[this.name]
        }).append($('<span>', {class: "hidden popup"}).html(this.messages["mandatory"])).append($('<label>', {value: data[this.title]}).html(this.messages[data[this.title]])).append($('<input>', {
            class: "form-control " + data[this.name],
            name: data[this.name],
            customType: data[this.typeForSaving] ? data[this.typeForSaving] : "",
            type: 'text',
            value: elementValue != null ? elementValue : "",
            readonly: !Utils.checkFieldEnabled(data)
        })).append($('<span>', {class: "input-group-addon"}).append($('<span>', {class: "glyphicon glyphicon-calendar"}))));

    $("#user-card-" + data[this.name]).datetimepicker({
        locale: 'uk'
    });

    if (data[this.mandatoryCondition] != "") {
        this.mandatoryCondtitions[data[this.name]] = data[this.mandatoryCondition];
    }

}

FormElements.prototype.multiSelect = function (data, parentElementId, elementValue) {
    var defaultRoles = [{"role": "ADMIN", "selected": false}, {"role": "USER", "selected": false}];

    if (elementValue != null) {
        for (var i in defaultRoles) {
            var name = defaultRoles[i].role;
            if (elementValue.indexOf(name) != -1) {
                defaultRoles[i].selected = true;
            }
        }
    }

    $('#' + parentElementId)
        .append($('<div>', {class: "textField" + " " + data[this.customClassName]})
            .append($('<span>', {class: "hidden popup"}).html(this.messages["mandatory"]))
            .append($('<label>', {value: data[this.title]}).html(this.messages[data[this.title]]))
            .append($('<select>', {
                class: "multiSelect form-control " + data[this.name],
                customType: data[this.typeForSaving] ? data[this.typeForSaving] : "",
                name: data[this.name],
                multiple: 'multiple',
                value: elementValue != null ? elementValue : "",
                disabled: !Utils.checkFieldEnabled(data)
            })
                .append(
                    $.map(defaultRoles, function (element, index) {
                        if (element.selected) {
                            return '<option value="' + element.role + '" selected="' + element.selected + '">' + element.role + '</option>';
                        } else {
                            return '<option value="' + element.role + '">' + element.role + '</option>';
                        }

                    }).join()
                )));

    if (data[this.mandatoryCondition] != "") {
        this.mandatoryCondtitions[data[this.name]] = data[this.mandatoryCondition];
    }

}


FormElements.prototype.image = function (data, parentElementId, elementValue) {

    if (this.getSearchParams("mode") != "view") {
        $('#' + parentElementId)
            .append($('<div>', {class: "textField" + " " + data[this.customClassName]}).append($('<span>', {class: "hidden popup"}).html(this.messages["mandatory"])).append($('<label>', {value: data[this.title]}).html(this.messages[data[this.title]]))
                .append($('<input>', {
                    class: "input-ghost",
                    id: this.name,
                    customType: data[this.typeForSaving] ? data[this.typeForSaving] : "",
                    type: "file",
                    fileValue: "",
                    name: data[this.name],
                    style: "visibility:hidden; height:0"
                }).change(function () {
                    $(this).next($(this)).find('input.form-control').val(($(this).val()).split('\\').pop());
                    var reader = new FileReader();
                    reader.readAsDataURL(this.files[0]);
                    reader.idFileelement = this.id;
                    var readerVar = (this, function (e, data) {
                        // $("#"+this.idFileelement).attr('fileValue', reader.result);
                        $("#" + this.idFileelement)[0].fileValue = reader.result;
                    });
                    reader.onload = readerVar;
                }))
                .append($('<div>', {class: "input-group input-file"})
                    .append($('<span>', {class: "input-group-btn"})
                        .append($('<button>', {class: "btn btn-default btn-choose", type: 'button'}).click(function () {
                            $('.input-ghost').click();
                        }).html(this.messages["choose"])))
                    .append($('<input>', {
                        class: "form-control",
                        placeholder: this.messages["chooseFile"],
                        type: 'text'
                    }).css("cursor", "pointer").mousedown(function () {
                        $(this).parents('.input-file').prev().click();
                    }))
                    .append($('<span>', {class: "input-group-btn", style: "float : left;"})
                        .append($('<button>', {class: "btn btn-warning btn-reset", type: 'button'}).click(function () {
                            $('.input-ghost').val(null);
                            $(this).parents(".input-file").find('input').val('');
                        }).html(this.messages["reset"])))));

        if (data[this.mandatoryCondition] != "") {
            this.mandatoryCondtitions[data[this.name]] = data[this.mandatoryCondition];
        }
    } else {
        $('#' + parentElementId).append($('<div>', {class: "textField" + " " + data[this.customClassName]}).append($('<span>', {class: "hidden popup"}).html(data[this.title])).append($('<label>', {value: data[this.title]}).html(this.messages[data[this.title]])).append($('<img>', {src: "data:" + elementValue[0]["data"].replace(" ", "+").replace(/\s/g, '+')})));
    }

}


FormElements.prototype.file = function (data, parentElementId, elementValue, addNew) {
    var scope = this;
    var data = data;
    var parentElementId = parentElementId;
    var elementValue = elementValue;

    var hasEmptyFields = Utils.checkIfHasEmptyFiles(data[this.name]);
    var filesCount = Utils.countAllFiles(data[this.name]);
    var limitSame = addNew && data.limit ? data.limit - 1 == filesCount : true;

    if (Utils.checkFieldEnabled(data) && !hasEmptyFields && limitSame) {
        var createdElement = $('<div>', {
            class: "textField" + " " + data.customClassName + "  " + data.name
        }).append($('<span>', {
            class: "hidden popup",
            html: this.messages["mandatory"]
        })).append($('<label>', {
                value: data[this.title],
                html: this.messages[data[this.title]]
            }))
            .append($('<input>', {
                class: "input-ghost",
                id: this.name + "_" + scope.countFormElements,
                customType: data[this.typeForSaving] ? data[this.typeForSaving] : "",
                type: "file",
                fileValue: "",
                name: data[this.name]
            }).change(function () {
                $(this).next($(this)).find('input.form-control').val(($(this).val()).split('\\').pop());
                var reader = new FileReader();
                reader.readAsDataURL(this.files[0]);
                reader.idFileelement = this.id;
                var readerVar = (this, function (e, data) {
                    $("#" + this.idFileelement)[0].fileValue = reader.result;
                });
                reader.onload = readerVar;
                scope.file(data, parentElementId, elementValue, true);
            }))
            .append($('<div>', {
                class: "input-group input-file"
            }).append($('<span>',
                {
                    class: "input-group-btn"
                }).append($('<button>', {
                    class: "btn btn-default btn-choose",
                    type: 'button'
                }).click(function () {
                    $(this).parents(".textField").find('.input-ghost').click();
                }).html(this.messages["choose"])))
                .append($('<input>', {
                    class: "form-control",
                    placeholder: this.messages["chooseFile"],
                    type: 'text'
                }).css("cursor", "pointer").mousedown(function () {
                    $(this).parents('.input-file').prev().click();
                }))
                .append($('<span>', {
                    class: "input-group-btn"
                }).append($('<button>', {
                    class: "btn btn-warning btn-reset",
                    type: 'button'
                }).click(function () {
                    var countEmptyFields = Utils.countEmptyFiles(data.name);

                    $(this).parents(".textField").find('.input-ghost').val(null);
                    $(this).parents(".input-file").find('input').val('');

                    if (countEmptyFields > 1) {
                        $(this).parents(".textField").remove()
                    }

                }).html(this.messages["reset"]))));

        if (addNew) {
            createdElement.insertAfter($("." + data[this.name] + "").last())
        } else {
            $('#' + parentElementId).append(createdElement);

            if (data[this.mandatoryCondition] != "") {
                this.mandatoryCondtitions[data[this.name]] = data[this.mandatoryCondition];
            }
        }

    }

    if (!addNew) {
        for (var i in elementValue) {
            $('#' + parentElementId)
                .append($('<div>', {class: "filesList textField" + " " + data[this.customClassName]})
                    .append($('<span>', {class: "hidden popup"}).html(data[this.title]))
                    .append($('<div>', {class: "fileSaver"})
                        .append($('<a>', {
                            fileData: elementValue[i]["data"].replace(" ", "+").replace(/\s/g, '+')
                        }).click(function () {
                            var element = document.createElement('a');
                            element.setAttribute('href', this.attributes.filedata.value);
                            element.setAttribute('download', this.innerHTML);

                            element.style.display = 'none';
                            document.body.appendChild(element);
                            element.click();
                            document.body.removeChild(element);
                        }).html(elementValue[i]["fileName"]))));
        }
    }


    scope.countFormElements++;

}


FormElements.prototype.userAssoc = function (data, parentElementId, elementValue) {
    var scope = this;
    var maxElements = data.maxElements != undefined ? data.maxElements : 100;
    var resultTable = ["firstName", "lastName"];
    $('#' + parentElementId)
        .append($('<div>', {class: "textField" + " " + data[this.customClassName]})
            .append($('<span>', {class: "hidden popup"})
                .html(this.messages["mandatory"]))
            .append($('<label>', {value: data[this.title]})
                .html(this.messages[data[this.title]])).append($('<input>', {
                maxElements: maxElements,
                class: "form-control " + data[this.name], name: data[this.name],
                placeholder: "search ...",
                customType: data[this.typeForSaving] ? data[this.typeForSaving] : "",
                type: 'text', value: "",
                readonly: !Utils.checkFieldEnabled(data)
            }).keyup(function (event) {
                scope.searchUsers(event, this);
            })).append($('<table>', {class: "added"})).append($('<table>', {class: "searchResult"})));

    if (data[this.mandatoryCondition] != "") {
        this.mandatoryCondtitions[data[this.name]] = data[this.mandatoryCondition];
    }

    if (!Utils.checkFieldEnabled(data)) {
        var addedTable = $('#' + parentElementId).find('table.added')[0];
        for (var index in elementValue) {
            var searchResultElement = elementValue[index];
            $(addedTable).append($('<tr>', {id: searchResultElement._id["$oid"]}).append(
                $.map(resultTable, function (elementName, index) {
                    return '<td>' + searchResultElement[elementName] + '</td>';
                }).join()
            ));
        }
    }
}

FormElements.prototype.searchUsers = function (event, data) {
    var resultTable = ["firstName", "lastName"];
    var table = $(data.parentNode).find('table.searchResult')[0];
    var addedTable = $(data.parentNode).find('table.added')[0];
    table.innerHTML = "";
    var searchValue = data.value;
    var userAssocInput = data;
    var scope = this;
    var maxElements = parseInt(data.attributes.maxelements.nodeValue);
    if (data.value != "") {
        // create headers
        /*
         var headerTR = $(table).append($('<tr>', {class: "header"}).append(
         $.map(resultTable, function (elementName, index) {
         return '<th>' + CUSTOM_MESSAGES["user.card." + elementName] + '</th>';
         }).join()
         ));
         */

        var search = {};
        var searchParams = {};
        searchParams.searchType = "user";
        searchParams.searchData = {};
        for (var index in resultTable) {
            searchParams.searchData[resultTable[index]] = searchValue;
        }
        search.search = searchParams;
        search.draw = 1;
        search.length = 100;
        search.start = 0;

        $.ajax({
            url: '/search-data',
            type: "POST",
            contentType: "application/json",
            data: encodeURI(JSON.stringify(search)),
            dataType: 'json'
        }).done(function (data) {
            if (data.data.length > 0) {
                for (var index in data.data) {
                    var searchResultElement = data.data[index];
                    $(table).append($('<tr>', {id: searchResultElement._id["$oid"]}).append(
                        $.map(resultTable, function (elementName, index) {
                            return '<td>' + searchResultElement[elementName] + '</td>';
                        }).join()
                    ).click(function () {
                        var addedRows = $(addedTable).find("tr").length;
                        if (addedRows < maxElements && $(addedTable).find("#" + this.id).length == 0) {
                            $(addedTable).append($(this.outerHTML).click(function () {
                                this.parentNode.removeChild(this);
                            }));
                            userAssocInput.value = "";
                            scope.searchUsers(event, userAssocInput);
                        }
                    }));
                }
            }
            console.log("done");
        });
    }

    console.log("aaa");
}


FormElements.prototype.comments = function (data, parentElementId, elementValue) {
    var scope = this;
    this.commentsMainElemId = parentElementId;
    this.commentsMainElemData = data;
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

    if (data[this.mandatoryCondition] != "") {
        this.mandatoryCondtitions[data[this.name]] = data[this.mandatoryCondition];
    }
}

FormElements.prototype.createCommentsList = function(commentsArr, parentElement){
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

FormElements.prototype.createCommentModalWindow = function(parentId){
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

FormElements.prototype.postComment = function(commentValue, parentElementId){
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



FormElements.prototype.getSearchParams = function getSearchParams(k) {
    var p = {};
    location.search.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (s, k, v) {
        p[k] = v
    })
    return k ? p[k] : p;
}