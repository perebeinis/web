function TextAreaComponent(){
}

TextAreaComponent.prototype.textArea = function(data, parentElementId, elementValue) {
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