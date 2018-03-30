function MenuElementsCretor(cardId, data, messages) {
    this.cardId = cardId;
    this.dataArray = data;
    this.customClassName = "customClassName";
    this.title = "title";
    this.name = "name";
    this.type = "type";
    this.mandatoryCondition = "mandatoryCondition";
    this.messages = messages;
    this.mandatoryCondtitions = {};
    this.alreadyOpened = false;

    this.createMenuElements = function () {
        var tabsCounter = 0;
        //create set for tabs
        var sets = this.dataArray;
        for (var i in sets){
            var set = sets[i];
            var name = set[this.name];
            var title = set[this.title];
            var type = set[this.type];

            if(type == "header"){
                this.createHeaderElement(set, cardId);
            }else{
                this.createFilterElement(set, cardId);
            }

        }
    }
    
    this.createHeaderElement = function (data, parentElement) {
        $('#'+parentElement)
            .append($('<li>').
             append($('<label>', {class: "tree-toggle nav-header glyphicon-icon-rpad"}).html(
                 this.messages[data[this.title]]!=undefined ? this.messages[data[this.title]] : data[this.title]).
             append($('<span>', {class: "menu-collapsible-icon glyphicon glyphicon-chevron-down"}))).
             append($('<ul>', {class: "nav nav-list tree bullets", id: data[this.name]})));

        var subElements = data["elements"];
        for (var i in subElements){
            this.createFilterElement(subElements[i], data[this.name]);
        }
    }


    this.createFilterElement = function (data, parentElement) {
        $('#'+parentElement)
            .append($('<li>').
            append($('<a>', {name: data[this.name], href : "#"}).html(
                this.messages[data[this.title]]!=undefined ? this.messages[data[this.title]] : data[this.title])
                .click(function() {
                    localStorage.setItem("lastOpenedFilter",this.name);
                    window.open("/search?filter="+this.name, "_self");
                })
            ));
    }


    /*
    this.createFilterElement = function (data, parentElement) {
        $('#'+parentElement)
            .append($('<li>').
            append($('<a>', {name: data[this.name], href : "#"}).html(
                this.messages[data[this.title]]!=undefined ? this.messages[data[this.title]] : data[this.title])
                .click(function() {
                    var link = "/search?filter="+this.name;
                    $.ajax({
                        url: link,
                        type: "GET",
                        contentType: "application/json",
                        data: encodeURI(JSON.stringify(search)),
                        dataType: 'json'
                    }).done(function (data) {
                        console.log("done");
                    });


                    history.pushState(null, null, link);
                })

            ));
    }
*/


    this.createMenuEvents = function () {
        var scope = this;
        $('.tree-toggle').click(function () {
            $(this).parent().children('ul.tree').toggle(200);
            scope.resizeNews();
        });

        $(function(){
            $('.tree-toggle').parent().children('ul.tree').toggle(200);
            scope.resizeNews();
        })

       if(!this.alreadyOpened){
           this.processLastOpenedMenuTree();
           this.alreadyOpened = true;
       }

    }

    this.resizeNews = function(){
        setTimeout(function() {
            var menuHeight = $(".left-menu-tree > .menu .container").height();
            var searcherMainBlockHeight = $(".main-block").height();
            var windowHeight = $(window).height();
            if(windowHeight > searcherMainBlockHeight) {
                $(".left-menu-tree .news").height(windowHeight - (menuHeight+150));
            }else {
                $(".left-menu-tree .news").height(searcherMainBlockHeight - (menuHeight+100));
            }
        }, 250);
    }
    
    this.processLastOpenedMenuTree = function () {
        var itemName = localStorage.getItem('lastOpenedFilter');
        var menuItem =  $('.container a[name="'+itemName+'"]')[0];
        var parent = menuItem.parentNode.parentNode;

        if($(parent).hasClass("bullets")){
            $(parent).parent().children('ul.tree').toggle(200);
        }

        if(!$(menuItem).hasClass("selected")){
            $(menuItem).addClass("selected");
        }

    }



    return this;
};

