function CardCreator(cardId, data, tabsId, cardFiledValues, messages) {
    this.cardId = cardId;
    this.tabsId = tabsId;
    this.cardFiledValues = cardFiledValues != undefined && cardFiledValues != null ? cardFiledValues : null;
    this.dataArray = data;
    this.customClassName = "customClassName";
    this.title = "title";
    this.name = "name";
    this.type = "type";
    this.mandatoryCondition = "mandatoryCondition";
    this.messages = messages;
    this.mandatoryCondtitions = {};
    this.typeForSaving = "typeForSaving";
    this.mode = this.getSearchParams("mode");
    this.countFormElements = 0;

    // Add components
    Object.assign(this, CommentsComponent.prototype);
    Object.assign(this, DateComponent.prototype);
    Object.assign(this, FileComponent.prototype);
    Object.assign(this, ImageComponent.prototype);
    Object.assign(this, MultiSelectComponent.prototype);
    Object.assign(this, TextAreaComponent.prototype);
    Object.assign(this, TextFieldComponent.prototype);
    Object.assign(this, UserAssocComponent.prototype);
    Object.assign(this, UserGroupAssocComponent.prototype);

    return this;
}

CardCreator.prototype.constructor = CardCreator;
CardCreator.prototype.createCardElements = function () {

    var tabsCounter = 0;
    //create set for tabs
    var tabs = this.dataArray.tabs;
    for (var i in tabs) {
        var set = tabs[i];
        var name = set[this.name];
        var title = set[this.title];

        var setId = this.cardId + "-" + name;
        var classNameTab = tabsCounter == 0 ? name + " tab-selected" : name;

        // Event when tad selected
        $('#' + this.tabsId).append($('<span>', {class: classNameTab, setId: setId}).click(function () {
            var setId = this.attributes.setid.value;
            if (!$(this).hasClass("tab-selected")) {
                $(".tab-selected").removeClass("tab-selected");
                $(".set-selected").removeClass("set-selected").addClass("hidden");
                $(".card-attributes-container .hidden").removeClass("tab-selected");
                $(this).addClass("tab-selected");
                $("#" + setId).addClass("set-selected").removeClass("hidden");
            }
        }).html(this.messages[title]!=undefined ? this.messages[title] : title));

        // Create set for tab
        var classNameSet = tabsCounter != 0 ? name + " hidden" : name + " set-selected";
        $('#' + this.cardId).append($('<div>', {class: classNameSet + "", id: setId}));

        //Create card fields
        var subArray = set["properties"];
        for (var j in subArray) {
            var attribute = subArray[j];
            var elementType = attribute[this.type];
            var elementValue = this.cardFiledValues != null && this.cardFiledValues[attribute[this.name]] != null ? this.cardFiledValues[attribute[this.name]] : null;
            this[elementType](attribute, setId, elementValue);
        }
        $('select').multipleSelect();
        tabsCounter++;

    }
}

CardCreator.prototype.getSearchParams = function getSearchParams(k) {
    var p = {};
    location.search.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (s, k, v) {
        p[k] = v
    })
    return k ? p[k] : p;
}


function CardButtonsCreator(parentId, data, cardAttributesObject, messages, mode, elementType) {

    this.parentId = parentId;
    this.dataArray = data;
    this.customClassName = "customClassName";
    this.title = "title";
    this.name = "name";
    this.type = "type";
    this.elementType = elementType;
    this.mode = mode;
    this.messages = messages;


    this.createCardButtons = function () {
        var tabsCounter = 0;
        //create buttons for cards
        var buttons = this.dataArray.buttons;
        for (var i in buttons) {
            var button = buttons[i];
            if (button.enableParams == undefined || Utils.checkFieldEnabled(button)) {
                var buttonType = button[this.type];
                this[buttonType](button, parentId);
                tabsCounter++;
            }
        }
    }

    this.button = function (data, parentElementId) {
        $('#' + parentElementId)
            .append($('<div>', {class: "card-button" + " " + data[this.customClassName]}).append($('<button>', {
                value: data[this.title],
                class: "btn btn-primary btn-md"
            }).click(this, this[data[this.name]])
                .html(this.messages[data[this.title]]!=undefined ? this.messages[data[this.title]] : data[this.title])));
    }

    this.sendNext = function (data, parentElementId) {
        window.open("/search?filter=news", "_self");
    }

    this.save = function (e, data) {
        var foundEmpty = e.data.mandatoryEventCheck();
        var currentElementType = e.data.elementType;

        if (!foundEmpty) {
            var postParams = [];
            
            // inputs
            $('.card-attributes-container input').each(function (data) {
                if (this.name != "" && this.name != "userAssoc") {
                    if (this.type == "file" && this.files.length>0) {
                        var postData = {
                            name: this.name,
                            type: this.attributes.customtype.nodeValue,
                            data: this.fileValue,
                            fileName: this.files[0].name
                        };
                        postParams.push(postData);
                        // postParams[this.name] = this.files[0].name +";"+this.fileValue;
                        //postParams[this.name] = this.fileValue;
                    } else if(this.value){
                        var postData = {name: this.name, type: this.attributes.customtype.nodeValue, data: this.value};
                        postParams.push(postData);
                        //postParams[this.name] = this.value;
                    }
                }
            });
            
            // textarea
            $('.card-attributes-container textarea').each(function (data) {
                if (this.name != "") {
                    var postData = {name: this.name, type: this.attributes.customtype.nodeValue, data: this.value};
                    postParams.push(postData);
                }
            });
            
            // select
            $(".card-attributes-container .multiSelect").each(function (data) {
                if (this.innerHTML != "" && this.name != undefined) {
                    var result = $(this.parentNode).find('.ms-drop.bottom > ul > li.selected:not(\'.ms-select-all\') > label > span').map(function () {
                        return this.innerHTML;
                    }).get().join();
                    var postData = {name: this.name, type: this.attributes.customtype.nodeValue, data: result};
                    postParams.push(postData);
                }
            });
            
            // userAssocs
            $('.card-attributes-container .userAssoc').each(function (data) {
                if (this.name != undefined) {
                    var result = $(this.parentNode).find('.added tr').map(function () {
                        return this.id;
                    }).get().join();
                    var postData = {name: this.name, type: this.attributes.customtype.nodeValue, data: result};
                    postParams.push(postData);
                }
            });
            

            $.ajax({
                url: '/create-new-element?type=' + currentElementType,
                type: "POST",
                contentType: "application/json",
                data: encodeURI(JSON.stringify(postParams)),
                dataType: 'json'
            }).done(function (data) {
                window.open("/get-element?type=" + currentElementType + "&mode=view&id=" + data._id, "_self");
            });
        }
    },

        this.sendNext = function (e, data) {
            var foundEmpty = e.data.mandatoryEventCheck();
            var currentElementType = e.data.elementType;

            if (!foundEmpty) {
                var postParams = [];

                // inputs
                $('.card-attributes-container input').each(function (data) {
                    if (this.name != "" && this.name != "userAssoc" && this.readOnly == false) {
                        if (this.type == "file" && this.files.length>0) {
                            var postData = {
                                name: this.name,
                                type: this.attributes.customtype.nodeValue,
                                data: this.fileValue,
                                fileName: this.files[0].name
                            };
                            postParams.push(postData);
                            // postParams[this.name] = this.files[0].name +";"+this.fileValue;
                            //postParams[this.name] = this.fileValue;
                        } else if(this.value){
                            var postData = {
                                name: this.name,
                                type: this.attributes.customtype.nodeValue,
                                data: this.value
                            };
                            postParams.push(postData);
                            //postParams[this.name] = this.value;
                        }
                    }
                });
                

                // textarea
                $('.card-attributes-container textarea').each(function (data) {
                    if (this.name != "" && this.readOnly == false) {
                        var postData = {name: this.name, type: this.attributes.customtype.nodeValue, data: this.value};
                        postParams.push(postData);
                    }
                });
                

                // select
                $(".card-attributes-container .multiSelect").each(function (data) {
                    if (this.innerHTML != "" && this.name != undefined) {
                        var result = $(this.parentNode).find('.ms-drop.bottom > ul > li.selected:not(\'.ms-select-all\') > label > span').map(function () {
                            return this.innerHTML;
                        }).get().join();
                        var postData = {name: this.name, type: this.attributes.customtype.nodeValue, data: result};
                        postParams.push(postData);
                    }
                });

                

                // userAssocs
                $('.card-attributes-container .userAssoc').each(function (data) {
                    if (this.name != undefined && this.readOnly == false) {
                        var result = $(this.parentNode).find('.added tr').map(function () {
                            return this.id;
                        }).get().join();
                        var postData = {name: this.name, type: this.attributes.customtype.nodeValue, data: result};
                        postParams.push(postData);
                    }
                });
                

                $.ajax({
                    url: '/update-element?type=' + currentElementType+"&id="+Utils.getUrlParameter("id"),
                    type: "POST",
                    contentType: "application/json",
                    data: encodeURI(JSON.stringify(postParams)),
                    dataType: 'json'
                }).done(function (data) {
                    window.open("/get-element?type=" + currentElementType + "&mode=view&id=" + data._id, "_self");
                });

                
            }


        }


    this.close = function () {
        window.open("/search?filter=news", "_self");
    }

    this.mandatoryEventCheck = function () {
        var conditionsList = cardAttributesObject.mandatoryCondtitions;
        for (var i in conditionsList) {
            var fieldName = i;
            var condition = conditionsList[i];
            var attribute = $('*[name=\'' + fieldName + '\']')[0];
            var mandatoryFound = false;
            if (attribute != undefined && condition!=undefined) {
                if (condition == "*") {
                    if (attribute.attributes.customtype.nodeValue == "userAssoc" && $(attribute.parentNode).find(".added > tr").length == 0) {
                        mandatoryFound = true;
                    } else if (attribute.attributes.customtype.nodeValue != "userAssoc" &&
                        (attribute.value == "" || (attribute.type == "file" && attribute.files.length == 0 ) || (attribute.type == "userAssoc" && attribute.files.length == 0 ))) {
                        mandatoryFound = true;
                    }
                } else {
                    var conditionJson = JSON.parse(condition.replace(new RegExp('&#39;', 'g'), '"'));
                    for (var j in conditionJson) {
                        var conditionAttribute = $('*[name=\'' + j + '\']')[0];
                        var conditionValue = conditionJson[j];
                        var regExp = /\(([^)]+)\)/;
                        var valuesArray = regExp.exec(conditionValue)[1].split(",");
                        var result = (conditionValue.indexOf("NOT IN") == -1) ?
                        valuesArray.indexOf(conditionAttribute.value) != -1
                            : valuesArray.indexOf(conditionAttribute.value) == -1;

                        if (conditionAttribute.value == "" || result) {
                            mandatoryFound = true;
                            break;
                        }
                    }
                }
            }


            if (mandatoryFound) {
                // Current opened tabs close
                $('.tab-selected').removeClass("tab-selected");
                $('.set-selected').removeClass("set-selected").addClass("hidden")


                $(attribute.parentNode.parentNode).removeClass("hidden").addClass("set-selected");
                $('*[setid=\'' + attribute.parentNode.parentNode.id + '\']').removeClass("hidden").addClass("tab-selected");

                // Select new tabs
                attribute.focus();
                $(attribute.parentNode).find('span.popup').removeClass("hidden");

                setTimeout(function () {
                    $(attribute.parentNode).find('span.popup').addClass("hidden");
                }, 2000);

                return true;
            }
        }

        return false;


    }


};

