function DateComponent(){
}

DateComponent.prototype.date = function(data, parentElementId, elementValue) {
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