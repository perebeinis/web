function TextFieldComponent(){
}

TextAreaComponent.prototype.textField = function (data, parentElementId, elementValue) {
    if(Utils.checkFieldCanDisplay(data)) {
        $('#' + parentElementId)
            .append($('<div>', {class: "textField" + " " + data[this.customClassName]})
                .append($('<span>', {class: "hidden popup"})
                    .html(this.messages["mandatory"]))
                .append($('<label>', {value: data[this.title]})
                    .html(this.messages[data[this.title]]))
                .append($('<input>', {
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
}
