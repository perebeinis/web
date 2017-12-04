function MenuElementsCretor(cardId, data, tabsId) {
    this.cardId = cardId;
    var dataStr = data.replace(new RegExp('&quot;', 'g'),'"');
    this.dataArray = JSON.parse(dataStr);
    this.customClassName = "customClassName";
    this.title = "title";
    this.name = "name";
    this.type = "type";
    this.mandatoryCondition = "mandatoryCondition";
    this.mandatoryCondtitions = {};

    this.createMenuElements = function () {
        var tabsCounter = 0;
        //create set for tabs
        var sets = this.dataArray;
        for (var i in sets){
            var set = sets[i];
            var name = set[this.name];
            var title = set[this.title];

            $('#'+cardId)
                .append($('<div>', {class: " menu-item"+" "+set[this.customClassName]}).
                append($('<a>').html(set[this.title])));


        }
    }
    return this;
};

