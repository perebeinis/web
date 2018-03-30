function NewsListCretor(cardId, data, messages) {
    this.cardId = cardId;
    this.newsObj = data;
    console.log(this.newsObj);
    this.customClassName = "customClassName";
    this.title = "title";
    this.name = "name";
    this.type = "type";
    this.mandatoryCondition = "mandatoryCondition";
    this.messages = messages;
    this.mandatoryCondtitions = {};
    this.alreadyOpened = false;

    this.createNewsList = function () {
       var array = this.newsObj.data;
        for (var i in array){
             var searchResult = array[i];
            $('#'+this.cardId)
                .append($('<li>').append($('<span>').click(searchResult,function (e, data) {
                    window.open("/get-element?type=news&mode=view&id=" + e.data._id["$oid"], "_blank")
                }).html(searchResult.elementName)).append($('<span>').html(searchResult.description)));
        }
    }

    return this;
};

