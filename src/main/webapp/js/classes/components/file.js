function FileComponent(){
}


FileComponent.prototype.file = function(data, parentElementId, elementValue, addNew) {
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