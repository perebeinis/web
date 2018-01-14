function FormElements() {
    this.customClassName = "customClassName";
    this.title = "title";
    this.name = "name";
    this.typeForSaving = "typeForSaving";
    this.type = "type";

    return this;
}

FormElements.prototype.textField = function (data, parentElementId, elementValue) {
    $('#'+parentElementId)
        .append($('<div>', {class: "textField"+" "+data[this.customClassName]}).
         append($('<span>', {class: "hidden popup"}).html(this.messages["mandatory"])).
         append($('<label>', {value: data[this.title]}).html(this.messages[data[this.title]])).
         append($('<input>', {class: "form-control "+data[this.name], name:data[this.name], customType: data[this.typeForSaving],
            type: 'text', value: elementValue!=null? elementValue : "", readonly: this.mode == "view"? true : false})));

    if(data[this.mandatoryCondition]!=""){
        this.mandatoryCondtitions[data[this.name]] = data[this.mandatoryCondition];
    }

}

FormElements.prototype.textArea = function (data, parentElementId, elementValue) {
    $('#'+parentElementId)
        .append($('<div>', {class: "form-group"+" "+data[this.customClassName]}).
         append($('<span>', {class: "hidden popup"}).html(this.messages["mandatory"])).
         append($('<label>', {value: data[this.title]}).html(this.messages[data[this.title]])).
         append($('<textarea>', {class: "form-control "+data[this.name], name:data[this.name],
            customType: data[this.typeForSaving], type: 'text',
            value: elementValue!=null? elementValue : "", readonly: this.mode == "view"? true : false}).text(elementValue!=null? elementValue : "")));

    if(data[this.mandatoryCondition]!=""){
        this.mandatoryCondtitions[data[this.name]] = data[this.mandatoryCondition];
    }

}


FormElements.prototype.date = function (data, parentElementId, elementValue) {
    $('#'+parentElementId)
        .append($('<div>', {class: "input-group date"+" "+data[this.customClassName], id: "user-card-"+data[this.name]}).
        append($('<span>', {class: "hidden popup"}).html(this.messages["mandatory"])).
        append($('<label>', {value: data[this.title]}).html(this.messages[data[this.title]])).
        append($('<input>', {class: "form-control "+data[this.name], name: data[this.name], customType: data[this.typeForSaving], type: 'text',
            value: elementValue!=null? elementValue : "", readonly: this.mode == "view"? true : false})).
        append($('<span>', {class: "input-group-addon"}).
        append($('<span>', {class: "glyphicon glyphicon-calendar"}))));

    $("#user-card-"+data[this.name]).datetimepicker({
        locale: 'uk'
    });

    if(data[this.mandatoryCondition]!=""){
        this.mandatoryCondtitions[data[this.name]] = data[this.mandatoryCondition];
    }

}

FormElements.prototype.multiSelect = function (data, parentElementId, elementValue) {
    var defaultRoles = [{"role" :"ADMIN", "selected" : false}, {"role" :"USER", "selected" : false}];

    if(elementValue!=null){
        for (var i in defaultRoles){
            var name = defaultRoles[i].role;
            if(elementValue.indexOf(name)!=-1){
                defaultRoles[i].selected = true;
            }
        }
    }

    $('#'+parentElementId)
        .append($('<div>', {class: "textField"+" "+data[this.customClassName]}).
        append($('<span>', {class: "hidden popup"}).html(this.messages["mandatory"])).
        append($('<label>', {value: data[this.title]}).html(this.messages[data[this.title]])).
        append($('<select>', {class: "multiSelect form-control "+data[this.name],  customType: data[this.typeForSaving], name:data[this.name], multiple: 'multiple',
            value: elementValue!=null? elementValue : "", disabled: this.mode == "view"? "disabled" : false})
            .append(
                $.map(defaultRoles, function (element, index) {
                    if(element.selected){
                        return '<option value="'+element.role+'" selected="'+element.selected+'">' + element.role + '</option>';
                    }else{
                        return '<option value="'+element.role+'">' + element.role + '</option>';
                    }

                }).join()

        )));

    if(data[this.mandatoryCondition]!=""){
        this.mandatoryCondtitions[data[this.name]] = data[this.mandatoryCondition];
    }

}


FormElements.prototype.image = function (data, parentElementId, elementValue) {

    if(this.getSearchParams("mode") != "view"){
        $('#'+parentElementId)
            .append($('<div>', {class: "textField"+" "+data[this.customClassName]}).
            append($('<span>', {class: "hidden popup"}).html(this.messages["mandatory"])).
            append($('<label>', {value: data[this.title]}).html(this.messages[data[this.title]]))
                .append($('<input>', {class: "input-ghost", id: this.name, customType: data[this.typeForSaving], type: "file", fileValue: "", name : data[this.name], style: "visibility:hidden; height:0" }).change(function(){
                    $(this).next($(this)).find('input.form-control').val(($(this).val()).split('\\').pop());
                    var reader = new FileReader();
                    reader.readAsDataURL(this.files[0]);
                    reader.idFileelement = this.id;
                    var readerVar = (this, function (e, data) {
                        // $("#"+this.idFileelement).attr('fileValue', reader.result);
                        $("#"+this.idFileelement)[0].fileValue = reader.result;
                    });
                    reader.onload = readerVar;
                }))
                .append($('<div>', {class: "input-group input-file"})
                    .append($('<span>', {class: "input-group-btn"})
                        .append($('<button>', {class: "btn btn-default btn-choose", type : 'button'}).click(function(){
                            $('.input-ghost').click();
                        }).html(this.messages["choose"])))
                    .append($('<input>', {class: "form-control", placeholder: this.messages["chooseFile"], type : 'text'}).css("cursor","pointer").mousedown(function() {
                        $(this).parents('.input-file').prev().click();
                    }))
                    .append($('<span>', {class: "input-group-btn", style : "float : left;"})
                        .append($('<button>', {class: "btn btn-warning btn-reset", type : 'button'}).click(function(){
                            $('.input-ghost').val(null);
                            $(this).parents(".input-file").find('input').val('');
                        }).html(this.messages["reset"])))));

        if(data[this.mandatoryCondition]!=""){
            this.mandatoryCondtitions[data[this.name]] = data[this.mandatoryCondition];
        }
    }else {
        $('#'+parentElementId).append($('<div>', {class: "textField"+" "+data[this.customClassName]}).
        append($('<span>', {class: "hidden popup"}).html(data[this.title])).
        append($('<label>', {value: data[this.title]}).html(this.messages[data[this.title]])).
        append($('<img>', {src: "data:"+elementValue[0]["data"].replace(" ","+").replace(/\s/g, '+')})));
    }



}


FormElements.prototype.userAssoc = function (data, parentElementId, elementValue) {
    var scope = this;
    $('#'+parentElementId)
        .append($('<div>', {class: "textField"+" "+data[this.customClassName]}).
        append($('<span>', {class: "hidden popup"}).html(this.messages["mandatory"])).
        append($('<label>', {value: data[this.title]}).html(this.messages[data[this.title]])).
        append($('<input>', {class: "form-control "+data[this.name], name:data[this.name],
            placeholder : "search ...",
            customType: data[this.typeForSaving],
            type: 'text', value: elementValue!=null? elementValue : "",
            readonly: this.mode == "view"? true : false})).keyup(function (event) {scope.searchUsers(event, this);}).
         append($('<table>'))
        );

    if(data[this.mandatoryCondition]!=""){
        this.mandatoryCondtitions[data[this.name]] = data[this.mandatoryCondition];
    }
}

FormElements.prototype.searchUsers =  function(event, data) {
    var resultTable = ["firstName","lastName"];
    var table = $(data.parentNode).find('table')[0];
    table.innerHTML = "";
    var headerTR = $(table).append($('<tr>', {class: "header"}).append(
        $.map(resultTable, function (elementName, index) {
            return '<th>' + elementName + '</th>';
        }).join()
    ));

    console.log("aaa");
}


FormElements.prototype.getSearchParams = function getSearchParams(k){
    var p={};
    location.search.replace(/[?&]+([^=&]+)=([^&]*)/gi,function(s,k,v){p[k]=v})
    return k?p[k]:p;
}