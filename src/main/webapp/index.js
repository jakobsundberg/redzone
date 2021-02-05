var username;

document.getElementById("loginSubmitButton").onclick = function(){
    username = document.getElementById("loginUsername").value;
    document.getElementById("loggedInArea").innerHTML = "Logged in as: " + username;
    document.getElementById("loginArea").style.display="none";
    document.getElementById("gamesArea").style.display="inherit";
    return false;
};

window.setInterval(function(){
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var table = document.getElementById("games");
            table.innerHTML = "";
            var data = JSON.parse(this.responseText);
            data.games.forEach(function (item, index) {
                var row = document.createElement("tr");
                table.appendChild(row);
                var idCell = document.createElement("td");
                idCell.innerHTML = item.id;
                row.appendChild(idCell);
                var playersCell = document.createElement("td");
                playersCell.innerHTML = item.players;
                row.appendChild(playersCell);
                var joinCell = document.createElement("td");
                if(item.players < 2){
                    var joinButton = document.createElement("button");
                    joinButton.innerHTML = "Join";
                    joinButton.onclick = function(){
                        joinGame(item.id);
                    }
                    joinCell.appendChild(joinButton);
                }
                row.appendChild(joinCell);

            });
        }
    };

    xhttp.open("GET", "/getGames", true);
    xhttp.send();
}, 1000);

document.getElementById("createGameButton").onclick = function(){
    document.getElementById("gamesArea").style.display="none";
    document.getElementById("gameArea").style.display="inherit";

    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var data = JSON.parse(this.responseText);
            var gameTitle = document.getElementById("gameTitle");
            gameTitle.innerHTML = "Game " + data.gameId;
        }
    };

    xhttp.open("GET", "/createGame?deckListId=0&username=" + username, true);
    xhttp.send();
};

function joinGame(gameId){
    document.getElementById("gamesArea").style.display="none";
    document.getElementById("gameArea").style.display="inherit";

    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var data = JSON.parse(this.responseText);
            var gameTitle = document.getElementById("gameTitle");
            gameTitle.innerHTML = "Game " + data.gameId;
        }
    };

    xhttp.open("GET", "/joinGame?deckListId=0&username=" + username + "&gameId=" + gameId, true);
    xhttp.send();
}