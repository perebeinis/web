function ChangePasswordComponent() {
}


ChangePasswordComponent.prototype.createChangePassModalWindow = function () {
    var scope = this;
    var modalWindowId = "dynamicModal";
    $('body').append(
        $('<div>', {
            id: modalWindowId,
            class: "modal fade",
            tabindex: "-1",
            role: "dialog",
            "aria-labelledby": "confirm-modal",
            "aria-hidden": "false"
        })
            .append($('<div>', {class: "modal-dialog"})
                .append($('<div>', {class: "modal-content"})
                    .append($('<div>', {class: "modal-header"})
                        .append($('<a>', {class: "close", "data-dismiss": "modal"}).html("×"))
                        .append($('<h4>').html(scope.messages["constants.header.changePass"])))
                    .append($('<div>', {class: "modal-body"})
                        .append($('<input>', {class: "form-control new-pass-first",  placeholder : scope.messages["constants.newPassFirst"]}))
                        .append($('<input>', {class: "form-control new-pass-second", placeholder : scope.messages["constants.newPassSecond"]})))
                    .append($('<div>', {class: "modal-footer"})
                        .append($('<span>', {
                            class: "btn btn-primary",
                            "data-dismiss": "modal"
                        }).html(scope.messages["constants.save"]).click(function () {
                            var newPass1 = $("#" + modalWindowId).find('input')[0].value;
                            var newPass2 = $("#" + modalWindowId).find('input')[1].value;
                            if (newPass1 != newPass2) {
                                Utils.createInfoPopup(modalWindowId, "constants.passNotSame");
                            }else {
                                 scope.postNewPass($("#" + modalWindowId).find('input')[1].value, "");
                            }

                        }))
                        .append($('<span>', {
                            class: "btn btn-primary",
                            "data-dismiss": "modal"
                        }).html(scope.messages["constants.close"]))
                    )
                )
            )
    );

    $("#" + modalWindowId).modal();
    $("#" + modalWindowId).modal('show');
    $("#" + modalWindowId).on('hidden.bs.modal', function (e) {
        $("#" + modalWindowId).remove();
    });
}

ChangePasswordComponent.prototype.postNewPass = function (newPassValue, parentId) {
    var parentId = parentId;
    var scope = this;
    var postData = new Object();
    postData["password"] = newPassValue;
    $.ajax({
        url: '/change-my-pass',
        type: "POST",
        contentType: "application/json",
        data: encodeURI(JSON.stringify(postData)),
        dataType: 'json'
    }).done(function (data) {
        if(data == "OK"){
            Utils.createInfoPopup(parentId, "constants.loginChanged");
            setTimeout(function() {
                window.open("/login", "_self");
            }, 3000);
        }
    }).fail(function() {
        Utils.createInfoPopup(parentId, "constants.fail");
    });
}