var username;
var gameId;
var cards = {};
var players = {};
var playerId;
var opponentId;
var phase;

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
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var data = JSON.parse(this.responseText);
            gameId = data.gameId;
            startGame(data.gameId);
        }
    };

    xhttp.open("GET", "/createGame?deckListId=0&username=" + username, true);
    xhttp.send();
};

function joinGame(gameIdParameter){
    gameId = gameIdParameter;
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var data = JSON.parse(this.responseText);
            startGame(data.gameId);
        }
    };

    xhttp.open("GET", "/joinGame?deckListId=0&username=" + username + "&gameId=" + gameId, true);
    xhttp.send();
}

function startGame(gameId){
    document.getElementById("gamesArea").style.display="none";
    document.getElementById("gameArea").style.display="inherit";
    var gameTitle = document.getElementById("gameTitle");
    gameTitle.innerHTML = "Game " + gameId;
    var nextMessage = 0;
    var eventPoller = window.setInterval(function(){
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                var data = JSON.parse(this.responseText);
                nextMessage += data.events.length;
                data.events.forEach(function(event){
                    switch(event.type){
                        case "Join":
                            handleJoinEvent(event);
                            break;
                        case "Draw":
                            handleDrawEvent(event);
                            break;
                        case "SetLifeTotal":
                            handleSetLifeTotalEvent(event);
                            break;
                        case "GameTurn":
                            handleGameTurnEvent(event);
                            break;
                        case "PlayerTurn":
                            handlePlayerTurnEvent(event);
                            break;
                        case "PhaseChange":
                            handlePhaseChangeEvent(event);
                            break;
                        case "Play":
                            handlePlayEvent(event);
                            break;
                        case "Activate":
                            handleActivateEvent(event);
                            break;
                        case "DeclareAttackers":
                            handleDeclareAttackersEvent(event);
                            break;
                        case "Tap":
                            handleTapEvent(event);
                            break;
                        case "Untap":
                            handleUntapEvent(event);
                            break;
                        case "ManaPool":
                            handleManaPoolEvent(event);
                            break;
                        case "Death":
                            handleDeathEvent(event);
                            break;
                        case "Victory":
                            handleVictoryEvent(event);
                            break;
                        case "ClearAttackers":
                            handleClearAttackersEvent(event);
                            break;
                        case "CardInfo":
                            handleCardInfoEvent(event);
                            break;
                        default:
                            console.log("Unhandld event: "+event.type);
                            break;
                    }
                });
            }
        };

        xhttp.open("GET", "/getGameEvents?gameId="+gameId+"&from="+nextMessage, true);
        xhttp.send();
    }, 100);
}

function handleJoinEvent(event){
    var player = {}
    player.id = event.playerId;
    player.name = event.playerName;
    players[event.playerId] = player;

    if(player.name == username){
        playerId = player.id;
        document.getElementById("playerNameArea").innerHTML = "Player name: " + player.name;
    }
    else{
        opponentId = player.id;
        document.getElementById("opponentNameArea").innerHTML = "Opponent name: " + player.name;
    }
}

function handleDrawEvent(event){
    var card = cards[event.cardId];
    var isPlayer = event.playerId == playerId;
    var handAreaId = isPlayer ? "playerHandArea" : "opponentHandArea";
    var handArea = document.getElementById(handAreaId);
    card.element = document.createElement("img");
    card.element.src = "http://gatherer.wizards.com/Handlers/Image.ashx?type=card&multiverseid=" + card.multiverseId;
    card.element.onclick = function(){
        var xhttp = new XMLHttpRequest();
        xhttp.open("GET", "/play?gameId="+gameId+"&cardId=" + card.id, true);
        xhttp.send();
    }
    handArea.prepend(card.element);
}

function handleSetLifeTotalEvent(event){
    if(event.playerId == playerId){
        document.getElementById("playerLifeArea").innerHTML = "Player life: " + event.lifeTotal;
    }
    else{
        document.getElementById("opponentLifeArea").innerHTML = "Opponent life: " + event.lifeTotal;
    }
}

function handleGameTurnEvent(event){
    document.getElementById("gameTurn").innerHTML = "Game turn: " + event.turn;
}

function handlePlayerTurnEvent(event){
    var player = players[event.playerId];
    document.getElementById("playerTurn").innerHTML = "Player turn: " + player.name;
}

function handlePhaseChangeEvent(event){
    phase = event.phase;
    document.getElementById("phaseName").innerHTML = "Phase: " + event.phase;
}

function handlePlayEvent(event){
    var card = cards[event.cardId];
    var isPlayer = event.playerId == playerId;
    var battlefieldAreaId = isPlayer ? "playerBattlefieldArea" : "opponentBattlefieldArea";
    var battlefieldArea = document.getElementById(battlefieldAreaId);
    battlefieldArea.appendChild(card.element);
    card.element.onclick = function(){
        if(phase == "COMBAT"){
            var xhttp = new XMLHttpRequest();
            xhttp.open("GET", "/declareAttacker?gameId="+gameId+"&creatureId=" + card.id+"&targetId="+opponentId, true);
            xhttp.send();
        }
        else{
            var xhttp = new XMLHttpRequest();
            xhttp.open("GET", "/activate?gameId="+gameId+"&cardId=" + card.id+"&activatedAbilityId="+card.activatedAbilityId, true);
            xhttp.send();
        }
    }
}

function handleActivateEvent(event){
}

function handleDeclareAttackersEvent(event){
    var card = cards[event.cardId];
    var isPlayer = event.playerId == playerId;
    var redZoneAreaId = isPlayer ? "playerRedZoneArea" : "opponentRedZoneArea";
    var redZoneArea = document.getElementById(redZoneAreaId);
    redZoneArea.prepend(card.element);
}

function handleTapEvent(event){
     var card = cards[event.cardId];
     card.tapped = true;
     card.element.style.transform = 'rotate(90deg)';
}

function handleUntapEvent(event){
     var card = cards[event.cardId];
     card.tapped = false;
     card.element.style.transform = '';
}

function handleManaPoolEvent(event){
    if(event.playerId == playerId){
        document.getElementById("playerManaArea").innerHTML = "Player mana: " + event.mana;
    }
    else{
        document.getElementById("opponentManaArea").innerHTML = "Opponent mana: " + event.mana;
    }
}

function handleDeathEvent(event){
    var player = players[event.playerId];
    alert(player.name+" has been eliminated");
}

function handleVictoryEvent(event){
     var player = players[event.playerId];
     alert(player.name+" won the game.");
}

function handleClearAttackersEvent(event){
    var card = cards[event.cardId];
    var isPlayer = event.playerId == playerId;
    var battlefieldAreaId = isPlayer ? "playerBattlefieldArea" : "opponentBattlefieldArea";
    var battlefieldArea = document.getElementById(battlefieldAreaId);
    battlefieldArea.prepend(card.element);
}

function handleCardInfoEvent(event){
    var card = {}
    card.id = event.cardId;
    card.name = event.cardName;
    card.multiverseId = event.multiverseId;
    card.activatedAbilityId = event.activatedAbilityId;
    cards[event.cardId] = card;
}

document.getElementById("passPriorityButton").onclick = function(){
    var xhttp = new XMLHttpRequest();
    xhttp.open("GET", "/passPriority?gameId=" + gameId, true);
    xhttp.send();
}