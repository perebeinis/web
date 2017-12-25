function HeaderButton(cardId, data) {
    this.cardId = cardId;
    var dataStr = data.replace(new RegExp('&quot;', 'g'),'"');
    this.dataArray = JSON.parse(dataStr);
    this.customClassName = "customClassName";
    this.title = "title";
    this.name = "name";
    this.type = "type";
    this.mandatoryCondition = "mandatoryCondition";


    this.createHeaderElements = function () {
        var tabsCounter = 0;
        //create set for tabs
        var sets = this.dataArray;
        for (var i in sets){
            var set = sets[i];
            var name = set[this.name];
            var title = set[this.title];
            var functionName = this[name] == undefined ? "defaultClick" : name;

            $('#'+cardId)
                .append($('<div>', {class: " navbar-header "+" "+name}).
                append($('<a>' ,{class: "navbar-brand "}).click(this[name]).html(set[this.title])));


        }
    }


    this.createNewTask = function() {
        window.open("http://localhost:8082/welcome", "_self");
    };

    this.createNewUser = function() {
        window.open("http://localhost:8082/create-element?type=user", "_self");
    };

    this.defaultClick = function() {
        alert("aaaaaaaaaaaaaaaaaa");
    };

    this.searchData = function () {
        window.open("http://localhost:8082/search", "_self");
    }


    /*

    if(this[buttonName] == undefined){
        $( "#"+headerButtonId+"" ).click(this.defaultClick);
    }else {
        $( "#"+headerButtonId+"" ).click(this[buttonName]);
    }
    */


};
