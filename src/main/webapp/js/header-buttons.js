function HeaderButton(cardId, data, messages,userData) {
    this.cardId = cardId;
    this.dataArray = data;
    this.customClassName = "customClassName";
    this.title = "title";
    this.name = "name";
    this.type = "type";
    this.mandatoryCondition = "mandatoryCondition";
    this.messages = messages;
    this.userData = userData!=undefined && userData!="" ? userData : undefined;
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
        var avatar = avatar && this.userData!=undefined && this.userData.avatar!=undefined ? "<img src='"+this.userData.avatar[0].data.toString().replace(/\s/g, '+')+"'/>" : "";
        var userFullName = useUserFullName && this.userData!=undefined ? avatar + this.userData.firstName + " "+ this.userData.lastName : this.messages[set[this.title]];

        $('#'+parentElement)
            .append($('<ul>', {class: " nav navbar-nav "})
                .append($('<li>', {class: "dropdown"})
                    .append($('<a>', {class: "dropdown-toggle", 'data-toggle' : "dropdown", href :"#"}).html(userFullName)
                        .append($('<span>', {class: "caret"})))
                    .append($('<ul>', {class: "dropdown-menu", id:  name}))));

        var subElements = data["elements"];
        for (var i in subElements){

            $('#'+name)
                .append($('<li>', { class : subElements[i].customClassName!=undefined ? subElements[i].customClassName : ""})
                    .append($('<a>', {userId: this.userData._id["$oid"]})
                        .click(this[subElements[i][this.name]])
                        .html(this.messages[subElements[i][this.title]]!=undefined ? this.messages[subElements[i][this.title]] : subElements[i][this.title])));

        }
    }

    this.createFilterElement = function (data, parentElement) {
        var name = data[this.name];
        $('#'+parentElement)
            .append($('<div>', {class: " navbar-header "+" "+name}).
            append($('<a>' ,{class: "navbar-brand "}).click(this[name])
                .html(this.messages[data[this.title]]!=undefined ? this.messages[data[this.title]] : data[this.title])));
    }


    this.UA = function() {
        window.open("/?lang=uk", "_self");
    };

    this.ENG = function() {
        window.open("/?lang=en", "_self");
    };

    this.signOut = function() {
        window.open("/logout", "_self");
    };

    this.myProfile = function() {
        window.open("/get-element?type=user&mode=view&id="+this.attributes.userid.nodeValue , "_self");
    };

    this.createNewTask = function() {
        window.open("/create-element?type=issue&mode=create", "_self");
    };

    this.createNewMessage = function() {
        window.open("/create-element?type=message&mode=create", "_self");
    };

    this.createNewNews = function() {
        window.open("/create-element?type=news&mode=create", "_blank");
    };

    this.createNewUser = function() {
        window.open("/create-element?type=user&mode=create", "_self");
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