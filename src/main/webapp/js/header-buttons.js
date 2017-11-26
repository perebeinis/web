function HeaderButton(headerButtonId, buttonName) {
    this.headerButtonId = headerButtonId;
    console.log(headerButtonId);


    this.createNewTask = function() {
        $.post("http://localhost:8080/create-new-user", {test: "asdasd"}, function(result){
            console.log("aaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        });
    };

    this.createNewUser = function() {
        alert("createNewUser");
    };

    this.defaultClick = function() {
        alert("aaaaaaaaaaaaaaaaaa");
    };


    if(this[buttonName] == undefined){
        $( "#"+headerButtonId+"" ).click(this.defaultClick);
    }else {
        $( "#"+headerButtonId+"" ).click(this[buttonName]);
    }

};
