function HeaderButton(headerButtonId, buttonName) {
    this.headerButtonId = headerButtonId;
    console.log(headerButtonId);


    this.createNewTask = function() {
       alert("createNewTask");
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
