function CardCreator(cardId, data) {
    this.cardId = cardId;
    var dataStr = data.replace(new RegExp('&quot;', 'g'),'"');
    this.dataArray = JSON.parse(dataStr);
    console.log("-------------------------");
    console.log(this.dataArray);

    this.createCardElements = function () {
        for (var i in this.dataArray){
            var attribute = this.dataArray[i];
            var elementType = attribute["type"];
            this[elementType](attribute,cardId);


        }

    }

    this.textField = function (data, parentElementId) {
        console.log("eeeeeeeeeeeeeeeeee");
        $('#'+parentElementId)
            .append($('<div>', {class: "textField"}).
            append($('<input>', {class: data["name"], value: data["name"], type: 'text'})));
    }


};
