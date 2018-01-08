function HeaderButton(cardId, data, messages,userData) {
    this.cardId = cardId;
    var dataStr = data.replace(new RegExp('&quot;', 'g'),'"');
    this.dataArray = JSON.parse(dataStr);
    this.customClassName = "customClassName";
    this.title = "title";
    this.name = "name";
    this.type = "type";
    this.mandatoryCondition = "mandatoryCondition";
    this.messages = JSON.parse(messages.replace(new RegExp('&quot;', 'g'),'"'));
    this.userData = userData!=undefined && userData!="" ? JSON.parse(userData.replace(new RegExp('&quot;', 'g'),'"')) : undefined;
    CUSTOM_MESSAGES = this.messages;


    this.createHeaderElements = function () {
        var tabsCounter = 0;
        //create set for tabs
        var sets = this.dataArray;
        for (var i in sets){
            var set = sets[i];

            if(set.type == "header"){
                this.createHeaderElement(set, cardId);
            }else{
                this.createFilterElement(set, cardId);
            }

        }
    }

    this.createHeaderElement = function (data, parentElement) {
        var set = data;
        var name = set[this.name];
        var title = set[this.title];
        var functionName = this[name] == undefined ? "defaultClick" : name;

        if(name == 'userData'){
            this.createUserData(set,parentElement, true, true);
        }else {
            this.createUserData(set,parentElement, false, false);
        }
    }

    this.createUserData = function (data, parentElement, avatar, useUserFullName) {
        var set = data;
        var name = set[this.name];
        var title = set[this.title];
        var functionName = this[name] == undefined ? "defaultClick" : name;
        var avatar = avatar && this.userData!=undefined? "<img src='"+this.userData.avatar.replace(/\s/g, '+')+"'/>" : "";
        var userFullName = useUserFullName && this.userData!=undefined ? avatar + this.userData.firstName + " "+ this.userData.lastName : this.messages[set[this.title]];

        $('#'+parentElement)
            .append($('<ul>', {class: " nav navbar-nav "})
                .append($('<li>', {class: "dropdown"})
                    .append($('<a>', {class: "dropdown-toggle", 'data-toggle' : "dropdown", href :"#"}).html(userFullName)
                        .append($('<span>', {class: "caret"})))
                    .append($('<ul>', {class: "dropdown-menu", id:  name}))));

        var subElements = data["subElements"];
        for (var i in subElements){

            $('#'+name)
                .append($('<li>')
                    .append($('<a>').click(this[subElements[i][this.name]]).html(this.messages[subElements[i][this.title]])));

        }
    }

    this.createFilterElement = function (data, parentElement) {
        var name = data[this.name];
        $('#'+parentElement)
            .append($('<div>', {class: " navbar-header "+" "+name}).
            append($('<a>' ,{class: "navbar-brand "}).click(this[name]).html(this.messages[data[this.title]])));
    }



    this.signOut = function() {
        window.open("/logout", "_self");
    };

    this.createNewTask = function() {
        window.open("/welcome", "_self");
    };

    this.createNewUser = function() {
        window.open("/create-element?type=user", "_self");
    };

    this.defaultClick = function() {
        alert("aaaaaaaaaaaaaaaaaa");
    };

    this.searchData = function () {
        window.open("/search", "_self");
    }


    /*

    if(this[buttonName] == undefined){
        $( "#"+headerButtonId+"" ).click(this.defaultClick);
    }else {
        $( "#"+headerButtonId+"" ).click(this[buttonName]);
    }
    */


};

var CUSTOM_MESSAGES = {};