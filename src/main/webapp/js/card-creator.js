function CardCreator(cardId, data, tabsId) {
    this.cardId = cardId;
    var dataStr = data.replace(new RegExp('&quot;', 'g'),'"');
    this.dataArray = JSON.parse(dataStr);
    console.log("-------------------------");
    console.log(this.dataArray);

    this.createCardElements = function () {
        var tabsCounter = 0;
        //create set
        var sets = this.dataArray.set;
        for (var i in sets){
            var set = sets[i];
            var name = set["name"];
            var title = set["title"];

            var setId = cardId+"-"+name;
            var classNameTab = tabsCounter == 0 ? name+" tab-selected": name;
            $('#'+tabsId).append($('<span>', {class: classNameTab, setId: setId}).click(function() {
                var setId = this.attributes.setid.value;
                if(!$(this).hasClass("tab-selected")){
                    $(".tab-selected").removeClass("tab-selected");
                    $(".set-selected").removeClass("set-selected").addClass("hidden");
                    $(".card-attributes-container .hidden").removeClass("tab-selected");
                    $(this).addClass("tab-selected");
                    $("#"+setId).addClass("set-selected").removeClass("hidden");
                }
            }).html(title));

            var classNameSet = tabsCounter != 0 ? name+" hidden": name+" set-selected";
            $('#'+cardId).append($('<div>', {class: classNameSet+"", id: setId}));

            var subArray = set["set"];
            for (var j in subArray){
                var attribute = subArray[j];
                var elementType = attribute["type"];
                this[elementType](attribute,setId);
            }

            tabsCounter++;

        }
    }


    this.createCardButtons = function () {
        var tabsCounter = 0;
        //create set
        var buttons = this.dataArray.buttons;
        for (var i in buttons){
            var set = buttons[i];
            var name = set["name"];
            var title = set["title"];

            var subArray = set["set"];
            for (var j in subArray){
                var attribute = subArray[j];
                var elementType = attribute["type"];
                this[elementType](attribute, cardId);
            }

            tabsCounter++;

        }
    }

    this.textField = function (data, parentElementId) {
        console.log("eeeeeeeeeeeeeeeeee");
        $('#'+parentElementId)
            .append($('<div>', {class: "textField"+" "+data["customClassName"]}).
            append($('<label>', {value: data["title"]}).html(data["title"])).
            append($('<input>', {class: data["name"], value: data["name"], type: 'text'})));
    }


    this.button = function (data, parentElementId) {
        console.log("button");
        $('#'+parentElementId)
            .append($('<div>', {class: "card-button"+" "+data["customClassName"]}).
            append($('<button>', {value: data["title"]}).html(data["title"])));
    }



};
