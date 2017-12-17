function FormElements() {
    this.customClassName = "customClassName";
    this.title = "title";
    this.name = "name";
    this.type = "type";

    return this;
}

FormElements.prototype.textField = function (data, parentElementId) {
    $('#'+parentElementId)
        .append($('<div>', {class: "textField"+" "+data[this.customClassName]}).
         append($('<span>', {class: "hidden popup"}).html(data[this.title])).
         append($('<label>', {value: data[this.title]}).html(data[this.title])).
         append($('<input>', {class: "form-control "+data[this.name], name:data[this.name], type: 'text'})));

    if(data[this.mandatoryCondition]!=""){
        this.mandatoryCondtitions[data[this.name]] = data[this.mandatoryCondition];
    }

}


FormElements.prototype.date = function (data, parentElementId) {
    $('#'+parentElementId)
        .append($('<div>', {class: "input-group date"+" "+data[this.customClassName], id: "user-card-"+data[this.name]}).
        append($('<span>', {class: "hidden popup"}).html(data[this.title])).
        append($('<label>', {value: data[this.title]}).html(data[this.title])).
        append($('<input>', {class: "form-control "+data[this.name], name: data[this.name], type: 'text'})).
        append($('<span>', {class: "input-group-addon"}).
        append($('<span>', {class: "glyphicon glyphicon-calendar"}))));

    $("#user-card-"+data[this.name]).datetimepicker({
        locale: 'uk'
    });

    if(data[this.mandatoryCondition]!=""){
        this.mandatoryCondtitions[data[this.name]] = data[this.mandatoryCondition];
    }

}