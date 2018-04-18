function ImageComponent(){
}

ImageComponent.prototype.image = function(data, parentElementId, elementValue) {

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
