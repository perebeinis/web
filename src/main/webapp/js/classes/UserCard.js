function UserCardCreator(cardId, data, tabsId, cardFiledValues) {
    this.cardId = cardId;
    this.tabsId = tabsId;
    this.cardFiledValues = cardFiledValues!=null ? JSON.parse( cardFiledValues.replace(new RegExp('&quot;', 'g'), '"')) : null;
    var dataStr = data.replace(new RegExp('&quot;', 'g'), '"');
    this.dataArray = JSON.parse(dataStr);
    this.customClassName = "customClassName";
    this.title = "title";
    this.name = "name";
    this.type = "type";
    this.mandatoryCondition = "mandatoryCondition";
    this.mandatoryCondtitions = {};
    return this;
}

UserCardCreator.prototype = Object.create(FormElements.prototype);
UserCardCreator.prototype.constructor = UserCardCreator;

UserCardCreator.prototype.createCardElements = function () {
        var tabsCounter = 0;
        //create set for tabs
        var sets = this.dataArray.set;
        for (var i in sets){
            var set = sets[i];
            var name = set[this.name];
            var title = set[this.title];

            var setId = this.cardId+"-"+name;
            var classNameTab = tabsCounter == 0 ? name+" tab-selected": name;

            // Event when tad selected
            $('#'+this.tabsId).append($('<span>', {class: classNameTab, setId: setId}).click(function() {
                var setId = this.attributes.setid.value;
                if(!$(this).hasClass("tab-selected")){
                    $(".tab-selected").removeClass("tab-selected");
                    $(".set-selected").removeClass("set-selected").addClass("hidden");
                    $(".card-attributes-container .hidden").removeClass("tab-selected");
                    $(this).addClass("tab-selected");
                    $("#"+setId).addClass("set-selected").removeClass("hidden");
                }
            }).html(title));

            // Create set for tab
            var classNameSet = tabsCounter != 0 ? name+" hidden": name+" set-selected";
            $('#'+this.cardId).append($('<div>', {class: classNameSet+"", id: setId}));

            //Create card fields
            var subArray = set["set"];
            for (var j in subArray){
                var attribute = subArray[j];
                var elementType = attribute[this.type];
                var elementValue = this.cardFiledValues!=null && this.cardFiledValues[attribute[this.name]]!=null ? this.cardFiledValues[attribute[this.name]] : null;
                this[elementType](attribute,setId,elementValue);
            }
            tabsCounter++;

        }
    }



function CardButtonsCreator(parentId, data, cardAttributesObject) {

    this.parentId = parentId;
    var dataStr = data.replace(new RegExp('&quot;', 'g'),'"');
    this.dataArray = JSON.parse(dataStr);

    this.customClassName = "customClassName";
    this.title = "title";
    this.name = "name";
    this.type = "type";


    this.createCardButtons = function () {
        var tabsCounter = 0;
        //create buttons for cards
        var buttons = this.dataArray.buttons;
        for (var i in buttons){
            var set = buttons[i];

            var subArray = set["set"];
            for (var j in subArray){
                var attribute = subArray[j];
                var elementType = attribute[this.type];
                this[elementType](attribute, parentId);
            }
            tabsCounter++;
        }
    }

    this.button = function (data, parentElementId) {
        $('#'+parentElementId)
            .append($('<div>', {class: "card-button"+" "+data[this.customClassName]}).
            append($('<button>', {value: data[this.title] , class: "btn btn-primary btn-md"}).click(this,this[data[this.name]]).html(data[this.title])));
    }

    this.save = function(e, data){
        var foundEmpty = e.data.mandatoryEventCheck();

        if(!foundEmpty){
            var postParams = {};

            $('.card-attributes-container input').each(function(data){
                if(this.name!=""){
                    postParams[this.name] = this.value;
                }
            });




            $.ajax ({
                url: '/create-new-element?type=user',
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(postParams),
                dataType: 'json'
            })
        }


    }

    this.close = function(){
        window.open("http://localhost:8082/welcome", "_self");
    }

    this.mandatoryEventCheck = function(){
        var conditionsList =  cardAttributesObject.mandatoryCondtitions;
        for(var i in conditionsList){
            var fieldName = i;
            var condition = conditionsList[i];
            var attribute = $('*[name=\''+fieldName+'\']')[0];
            var mandatoryFound = false;
            if(condition == "*"){
                if(attribute.value == ""){
                    mandatoryFound = true;
                }
            }else {
                var conditionJson = JSON.parse(condition.replace(new RegExp('&#39;', 'g'),'"'));
                for (var j in conditionJson){
                    var conditionAttribute = $('*[name=\''+j+'\']')[0];
                    var conditionValue = conditionJson[j];
                    var regExp = /\(([^)]+)\)/;
                    var valuesArray = regExp.exec(conditionValue)[1].split(",");
                    var result = (conditionValue.indexOf("NOT IN") == -1) ?
                        valuesArray.indexOf(conditionAttribute.value)!=-1
                        : valuesArray.indexOf(conditionAttribute.value)==-1;

                    if(conditionAttribute.value=="" || result){
                        mandatoryFound = true;
                        break;
                    }


                }
            }


            if(mandatoryFound){

                // Current opened tabs close
                $('.tab-selected').removeClass("tab-selected");
                $('.set-selected').removeClass("set-selected").addClass("hidden")


                $(attribute.parentNode.parentNode).removeClass("hidden").addClass("set-selected");
                $('*[setid=\''+attribute.parentNode.parentNode.id+'\']').removeClass("hidden").addClass("tab-selected");

                // Select new tabs
                attribute.focus();
                $(attribute.parentNode).find('span.popup').removeClass("hidden");

                setTimeout(function(){
                    $(attribute.parentNode).find('span.popup').addClass("hidden");
                }, 2000);

                return true;
            }
        }

        return false;


    }


};

