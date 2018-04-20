var Utils = {
    checkFieldEnabled: function checkFieldEnabled(fieldData) {
        var enable = false;
        if (fieldData.enableParams) {
            var enableParams = fieldData.enableParams;
            for (var i in enableParams) {
                var parameter = enableParams[i];
                if (parameter.mode && Utils.getUrlParameter("mode") == parameter.mode) {
                    enable = true;
                    break;
                }

            }
        }

        return enable;
    },


    checkFieldCanDisplay: function checkFieldCanDisplay(fieldData) {
        var display = false;
        if (fieldData.viewParams) {
            var viewParams = fieldData.viewParams;
            for (var i in viewParams) {
                var parameter = viewParams[i];
                if (parameter.mode && Utils.getUrlParameter("mode") == parameter.mode) {
                    display = true;
                    break;
                }

            }
        } else {
            display = true;
        }

        return display;
    },

    getUrlParameter: function getUrlParameter(k) {
        var p = {};
        location.search.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (s, k, v) {
            p[k] = v
        })
        return k ? p[k] : p;
    },

    checkIfHasEmptyFiles: function checkIfHasEmptyFiles(className) {
        var hasEmptyFiles = false;
        $("." + className + " input[type=file]").each(function (data) {
            if (this.files.length == 0) {
                hasEmptyFiles = true;
            }
        });
        return hasEmptyFiles;
    },

    countEmptyFiles: function countEmptyFiles(className) {
        var hasEmptyFiles = 0;
        $("." + className + " input[type=file]").each(function (data) {
            if (this.files.length == 0) {
                hasEmptyFiles++;
            }
        });
        return hasEmptyFiles;
    },

    countAllFiles: function countEmptyFiles(className) {
        var filesCount = 0;
        $("." + className + " input[type=file]").each(function (data) {
            filesCount++;
        });
        return filesCount;
    },


    createInfoPopup : function createInfoPopup(parentId, message){
        var modalWindowId = "infoPopup";
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
                        .append($('<div>', {class: "modal-body"})
                            .append($('<span>', {class: "form-control info-popup"}).html(CUSTOM_MESSAGES[message])))
                    )
                )
        );

        $("#" + modalWindowId).modal();
        $("#" + modalWindowId).modal('show');

        setTimeout(function() {
            $("#" + modalWindowId).remove();
            $(".modal-backdrop").remove();
        }, 3000);

    }
}