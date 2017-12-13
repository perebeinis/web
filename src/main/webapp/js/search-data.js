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

        this.createSearchTable("");
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
            append($('<input>', {class: data[this.name], name:data[this.name], type: 'text'})));


    }


    this.createSearchTable = function (result) {
        var searchType = {};
        searchType["searchType"] = "user";
        var searchData = {};
        searchData["aaa"] = "trs";
        searchType["searchData"] = searchData;
        searchType["searchData"] = searchData;

        $(document).ready(function(){
            var table = $("#"+tableDivId).DataTable( {
                "processing": true,
                "serverSide": true,
                "ajax": {
                    "url": "/search-data",
                    "type": "POST",
                    "contentType": "application/json",
                    data:function(d){
                            d.search = searchType;
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
