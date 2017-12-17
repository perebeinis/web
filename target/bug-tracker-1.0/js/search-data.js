function SearchDataComponent(tableDivId, data) {
    this.tableDivId = tableDivId;
    var dataStr = data.replace(new RegExp('&quot;', 'g'),'"');
    this.searchersArray = JSON.parse(dataStr);
    this.customClassName = "customClassName";
    this.title = "title";
    this.name = "name";
    this.type = "type";
    this.mandatoryCondition = "mandatoryCondition";


    this.searchData = function () {
        this.createSearchers();
        this.createSearchButtons();
        var searchType = {};
        searchType["searchType"] = "user";

        var searchData = {};
        searchData["aaa"] = "trs";

        searchType["searchData"] = searchData;
        console.log("serchhhhhhhhhhhhhhhhhh");

        var scope = this;

        var test = $("#"+tableDivId).append($('<thead>').append($('<tr>')
            .append($('<td>').html("firstName"))
            .append($('<td>').html("lastName"))
            .append($('<td>').html("user_id"))
        ));


        var searchType = {};
        searchType["searchType"] = "user";
        var searchData = {};
        searchType["searchData"] = searchData;
        this.createSearchTable(searchType, this);
    }

    this.createSearchers = function () {
        var searchersId = "search-attributes";
        var subArray = this.searchersArray;
        for (var j in subArray){
            var attribute = subArray[j];
            var elementType = attribute[this.type];
            this[elementType](attribute, searchersId);
        }
    }

    this.text = function (data, parentElementId) {
        $('#'+parentElementId)
           .append($('<div>', {class: "textField"+" "+data[this.customClassName]}).
            append($('<span>', {class: "hidden popup"}).html(data[this.title])).
            append($('<label>', {value: data[this.title]}).html(data[this.title])).
            append($('<input>', {class: "form-control "+data[this.name], name:data[this.name], type: 'text', value : ''})));


    }

    this.createSearchButtons = function(){
        var scope = this;
        var searchButtonsDiv = "search-buttons";
        // search button
        $('#'+searchButtonsDiv).append($('<button>', {class: "searchButton btn btn-primary", value: "Search"}).html("Search").
        click(this, function(e) {
            e.data.searchDataEv();
        }));

        // search button
        $('#'+searchButtonsDiv).append($('<button>', {class: "clearButton btn btn-primary", value: "Clear"}).html("Clear").
        click(this, function(e) {
            e.data.clearDataEv();
        }));
    }

    this.searchDataEv = function(e,data){
        var searchAttrsDiv = "search-attributes";
        var searchData = {};
        $('#'+searchAttrsDiv+' input:not([name=""])').each(function() {
            if(this.value!=""){
                searchData[this.name] = this.value;
            }
        });
        var searchDataJson = {};
        searchDataJson["searchType"] = "user";
        searchDataJson["searchData"] = searchData;
        this.searchData = searchDataJson;
        this.table.ajax.reload();
        console.log("search");
    }

    this.clearDataEv = function(e,data){
        console.log("clearData");
    }


    this.createSearchTable = function (data, scope) {
        scope.searchData = data;

        $(document).ready(function(){
            scope.table = $("#"+tableDivId).DataTable( {
                "processing": true,
                "serverSide": true,
                "ajax": {
                    "url": "/search-data",
                    "type": "POST",
                    "contentType": "application/json",
                    data:function(d){
                            d.search =  scope.searchData;
                        return JSON.stringify(d);
                    },
                    "dataType": 'json'
                },
                "columns": [
                    { "data": "firstName"},
                    { "data": "lastName"},
                    { "data": "user_id"}
                ]
            });
        });

    }



    return this;
};
