function MultiSelectComponent(){
}

MultiSelectComponent.prototype.multiSelect = function(data, parentElementId, elementValue) {
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