import React from 'react';
import { AvTimer, FaceRetouchingNatural, Flare, HeartBroken, ModeEdit, PersonRemove, PersonRemoveAlt1, PersonRemoveAlt1TwoTone } from '@mui/icons-material';

import {List, Paper, Button, TextField} from '@mui/material';
import {editAllInitiativesPopup} from '../components/InitiativePopups.js';
import {returnCharacterEditPopup, returnCharacterViewPopup} from '../components/EditCharacterPopups.js';
import {removeFromMapV2} from '../pages/SinglePlayerMenu.js';

class GamePlayMenu extends React.Component {

    constructor(props) {
        super(props);

        const queryParams = new URLSearchParams(window.location.search);
        const user = queryParams.get('user');
        const dm = queryParams.get('isDM');
        const currChar = queryParams.get('currentCharacter');
        console.log('got user: ' + user + ', DM: ' + dm + ', current character: ' + currChar);
   
        this.state = {
            currentPlayer: user,
            dm: dm,
            currentCharacter: currChar,
            currentTurn: "",
            nextTurn: "",
            timeString: "",
            initiativeOrder: {},
            modal1Open: false,
            modal2Open: false,
            modal3Open: false,
            modal4Open: false,
            modal5Open: false,
            timedEffects: {},
            timedEffectsPresent: false,
            currentHealthBeingEditedName: "",
            currentCharacterBeingEdited: {},
            currentCharacterBeingEditedName: "",
        };

        this.getInitiative.bind(this);
        this.getInitiative();

        this.getCharacterForView.bind(this);

        this.getTurns.bind(this);
        this.getTurns();

        this.doNextTurn.bind(this);

        this.getTime.bind(this);
        this.getTime();

        this.getTimedEffects.bind(this);
        this.getTimedEffects();
    }

    getInitiative = async () => {
        const res = await fetch(window.apiURL + '/playermenu/getinitiative', {
            method: 'GET',
        })
        .then(response => response.json())
        .then(response => {
            this.setState({
                initiativeOrder: response,
            });
        })
        .catch(err => {
            console.log(err);
        });

        console.log('got initiative order');
    }

    getCharacterForView = async () => {
        // populate PCs in map, then open the initiative popup!
        console.log('getting ' + this.state.currentCharacterBeingEditedName + ' (to edit) ...')

        try {
            const response = await fetch(window.apiURL + '/playermenu/getcharacter/' + this.state.currentCharacterBeingEditedName, {
                method: 'GET'
            })
            var char = await response.json();

            setTimeout(() => {  
                this.setState({
                    currentCharacterBeingEdited: char,
                    modal3Open: true,
                });
            }, 10);
            
        }
        catch(err) {
            console.log(err);
        }
        
    }

    getTurns = async () => {
        try {

            const res = await fetch(window.apiURL + '/playermenu/getcurrentcharacter', {
                method: 'GET',
            })
            .then(response => response.text())
            .then(response => {
                // console.log('response:', response)
                this.setState({
                    currentTurn: response,
                });
            })
            .catch(err => {
                console.log(err);
            });

            const res2 = await fetch(window.apiURL + '/playermenu/getnextcharacter', {
                method: 'GET',
            })
            .then(response => response.text())
            .then(response => {
                // console.log('response:', response)
                this.setState({
                    nextTurn: response,
                });
            })
            .catch(err => {
                console.log(err);
            });
            console.log('current turn:', this.state.currentTurn, ', next turn:', this.state.nextTurn)
        }
        catch(err) {
            console.log(err);
        }
        
    }

    getTime = async () => {
        const res = await  fetch(window.apiURL + '/playermenu/gettimestring', {
            method: 'GET',
        })
        .then((response) => {
            // console.log(response)
            return response.text()
        })
        .then((response) => {
            // console.log(response)
            this.setState({
                timeString: response,
            });
        })
        .catch(err => {
            console.log(err);
        });
    }

    doNextTurn = async () => {
        const res = await  fetch(window.apiURL + '/playermenu/nextturn', {
            method: 'GET',
        })
        .then(() => {
            this.getTurns();
            this.getTime();
            this.getTimedEffects();
        })
        .catch(err => {
            console.log(err);
        });
    }

    getTimedEffects = async () => {
        const res = await fetch(window.apiURL + '/playermenu/gettimedeffects', {
            method: 'GET',
        })
        .then(response => response.json())
        .then(response => {
            this.setState({
                timedEffects: response,
            });
        })
        .catch(err => {
            console.log(err);
        });

        if (this.state.timedEffects == {}) {
            this.setState({
                timedEffectsPresent: false,
            });
        } else {
            this.setState({
                timedEffectsPresent: true,
            });
        }

        console.log('got effects:', this.state.timedEffects);
    }

    render() {

        // todo:

        // josh hunger/fatigue with turn timer
        // josh stats / things / inventory, etc

        // --- --- --- extra todo:

        // josh sinlgeplayer menu (+single player char save check) / DM permissions
        // josh mobile menus

        // --- --- --- --- even more extra stuff:

        // josh hex map? (replaces most UI, current UI becomes pop-ups)
        // (size has to become a dropdown menu)

        // --- --- --- --- --- far future stuff:

        // online connections, race condition protection?

        return (
            <div>
                {/* Popups: */}

                <div className="modal1" style={{display: this.state.modal1Open ? 'block' : 'none', "position": "absolute", "zIndex": "1", "left": "38%", "top":"15%", resizeMode: 'center'}}>
                    <div style={{"position": "fixed", "backgroundColor": "lightgrey", border: '5px solid lightgrey',borderRadius: '5px'}}>
                        {this.state.modal1Open ? 
                            <div style={{'backgroundColor':'white', border: '10px solid white', borderRadius: '5px'}}>
                                <div>
                                    <TextField
                                        onChange={getCharHealth}
                                        required
                                        id="outlined-required"
                                        type="number"
                                        label="Damage dealt"
                                        defaultValue={0}
                                    />
                                </div>
                                <p></p>
                                <div>
                                    <Button color="third" onClick={() => {
                                        editCharacterHealth(this.state.currentHealthBeingEditedName)
                                        // this.getInitiative();
                                        // setTimeout(() => {  
                                        //     const newData = this.state.initiativeOrder.slice(0);
                                        //     newData[0] = 'something'
                                        //     this.setState({
                                        //         initiativeOrder: newData,
                                        //     });
                                        // }, 10);
                                        // this.setState({modal1Open: false,})
                                    }} variant="contained" endIcon={<HeartBroken />}>
                                        Deal damage
                                    </Button>
                                </div>
                            </div>
                        : 'Modal1'}
                        <Button style={{"color":"black", textTransform: "none", fontSize: "10px"}} onClick={() => this.setState({modal1Open: false,})}>[close]</Button>
                    </div>
                </div>
                <div className="modal2" style={{display: this.state.modal2Open ? 'block' : 'none', "position": "absolute", "zIndex": "1", "top":"15%", resizeMode: 'center'}}>
                    <div style={{"position": "fixed", "backgroundColor": "lightgrey", maxWidth: '84%', border: '5px solid lightgrey',borderRadius: '5px'}}>
                        {this.state.modal2Open ? 
                            editAllInitiativesPopup(this.state.initiativeOrder)
                        : 'Modal2'}
                        <Button style={{"color":"black", textTransform: "none", fontSize: "10px"}} onClick={() => this.setState({modal2Open: false,})}>[close]</Button>
                    </div>
                </div>
                <div className="modal3" style={{display: this.state.modal3Open ? 'block' : 'none', "position": "absolute", "top":"1%", "zIndex": "1", resizeMode: 'center'}}>
                    <div style={{"position": "center", width:'90%', "backgroundColor": "lightgrey", border: '5px solid lightgrey', borderRadius: '5px'}}>
                        {this.state.modal3Open ? 
                            returnCharacterViewPopup(this.state.currentCharacterBeingEdited)
                        : 'Modal3'}
                        <div style={{"alignItems":"center", "justifyContent":"center", "display": "flex", "flexDirection": "row", border: '5px solid lightgrey',borderRadius: '5px'}}>
                            <Button color="deeppurp" style={{"color":"black", textTransform: "none", fontSize: "10px", color:'white'}} onClick={() => this.setState({modal3Open: false, modal4Open: true,})} variant="contained" endIcon={<FaceRetouchingNatural />}>
                                Edit {this.state.currentCharacterBeingEditedName}
                            </Button>
                            <Button style={{"color":"black", textTransform: "none", fontSize: "10px"}} onClick={() => this.setState({modal3Open: false,})}>[close]</Button>
                        </div>
                    </div>
                </div>
                <div className="modal4" style={{display: this.state.modal4Open ? 'block' : 'none', "position": "absolute", "top":"1%", "zIndex": "1", resizeMode: 'center'}}>
                    <div style={{"position": "center", width:'90%', "backgroundColor": "lightgrey", border: '5px solid lightgrey', borderRadius: '5px'}}>
                        {this.state.modal4Open ? 
                            returnCharacterEditPopup(this.state.currentCharacterBeingEdited)
                        : 'Modal4'}
                        <Button style={{"color":"black", textTransform: "none", fontSize: "10px"}} onClick={() => this.setState({modal4Open: false,})}>[close]</Button>
                    </div>
                </div>
                <div className="modal5" style={{display: this.state.modal5Open ? 'block' : 'none', "position": "absolute", "left": "28%", "top":"15%", "zIndex": "1", resizeMode: 'center'}}>
                    <div style={{"position": "fixed", "backgroundColor": "lightgrey", border: '5px solid lightgrey',borderRadius: '5px'}}>
                        {this.state.modal5Open ? 
                            <Paper style={{maxHeight: 800, maxWidth: 800, overflow: 'auto', 'backgroundColor':'white'}}>
                                <p>Add a timed effect:</p>
                                <div style={{"display": "flex", "flexDirection": "row", margin:'10px'}}>
                                    &nbsp;
                                    <TextField color="secondary"
                                        onChange={setEffectName}
                                        style = {{width: '50%', color: 'black'}}
                                        id="outlined"
                                        required
                                        InputLabelProps={{ style: {zIndex: 0} }}
                                        label="Effect name"
                                    />&nbsp;&nbsp;&nbsp;&nbsp;
                                    <TextField color="sixth"
                                        onChange={setEffect}
                                        style = {{width: '70%', color: 'black'}}
                                        id="outlined"
                                        InputLabelProps={{ style: {zIndex: 0} }}
                                        label="Effect"
                                    />&nbsp;&nbsp;&nbsp;&nbsp;
                                    <TextField color="sixth"
                                        onChange={setEffectTargets}
                                        style = {{width: '50%', color: 'black'}}
                                        id="outlined"
                                        InputLabelProps={{ style: {zIndex: 0} }}
                                        label="Targets/Area"
                                    />&nbsp;
                                </div>
                                <p>Effect duration:</p>
                                <div style={{"display": "flex", "flexDirection": "row", margin:'10px'}}>
                                    &nbsp;
                                    <TextField color="ninth"
                                        onChange={setEffectRounds}
                                        style = {{width: '30%', color: 'black'}}
                                        id="outlined"
                                        InputLabelProps={{ style: {zIndex: 0} }}
                                        label="Rounds"
                                        type="number"
                                        defaultValue={0}
                                    />&nbsp;&nbsp;&nbsp;&nbsp;
                                    <TextField color="ninth"
                                        onChange={setEffectMinutes}
                                        style = {{width: '30%', color: 'black'}}
                                        id="outlined"
                                        InputLabelProps={{ style: {zIndex: 0} }}
                                        label="Minutes"
                                        type="number"
                                        defaultValue={0}
                                    />&nbsp;&nbsp;&nbsp;&nbsp;
                                    <TextField color="ninth"
                                        onChange={setEffectHours}
                                        style = {{width: '30%', color: 'black'}}
                                        id="outlined"
                                        InputLabelProps={{ style: {zIndex: 0} }}
                                        label="Hours"
                                        type="number"
                                        defaultValue={0}
                                    />&nbsp;&nbsp;&nbsp;&nbsp;
                                    <TextField color="ninth"
                                        onChange={setEffectDays}
                                        style = {{width: '30%', color: 'black'}}
                                        id="outlined"
                                        InputLabelProps={{ style: {zIndex: 0} }}
                                        label="Days"
                                        type="number"
                                        defaultValue={0}
                                    />&nbsp;
                                </div>
                                <Button color="primary" style={{textTransform: "none", fontSize: "12px", color:'black', margin:'10px'}} onClick={() => {
                                    timedEffectCreateButton()
                                    this.setState({modal5Open: false,})
                                    setTimeout(() => {  
                                        this.getTimedEffects();
                                    }, 10);
                                }} variant="contained" endIcon={<AvTimer />}>
                                    Start effect
                                </Button>
                           
                            </Paper>
                        : 'Modal5'}
                        <Button style={{"color":"black", textTransform: "none", fontSize: "10px"}} onClick={() => this.setState({modal5Open: false,})}>[close]</Button>
                    </div>
                </div>
                <div style={{resizeMode: 'center'}}>
                    <Paper style={{maxHeight: 820, overflow: 'auto', backgroundColor: 'transparent'}}>
                        <div style={{border: '15px solid transparent', borderRadius: '5px'}}>
                            <TextField color="dmblue"
                                size="small"
                                focused
                                style = {{width: '50%'}}
                                InputProps={{
                                    readOnly: true,
                                    style: { fontSize: 18, color: '#283593' },
                                }}
                                id="outlined"
                                InputLabelProps={{ style: {fontSize: 16, zIndex: 0} }}
                                label="Time"
                                value={this.state.timeString}
                            />
                            <p></p>
                            <div style={{"flexDirection": "row"}}>
                                <Button size="small" color="primary" variant="contained" style={{color:'black', textTransform: "none", fontSize: "16px"}} onClick={() => this.doNextTurn()}>
                                    Next Turn
                                </Button>&nbsp;&nbsp;&nbsp;&nbsp;
                                <Button size="small" variant="contained" color="editblue" style={{"color":"black",  textTransform: "none", fontSize: "16px"}} onClick={() => {
                                    this.setState({
                                        modal2Open: true
                                    });
                                }}>
                                    Edit Initiative Order
                                </Button>&nbsp;&nbsp;&nbsp;&nbsp;
                                <Button size="small" variant="contained" color="dmblue" style={{"color":"white",  textTransform: "none", fontSize: "16px"}} onClick={() => {
                                    this.setState({
                                        modal5Open: true
                                    });
                                }}>
                                    Add a timed effect
                                </Button>
                            </div>
                            <p></p>
                            <Paper style={{maxHeight: 200, maxWidth: 1100, overflow: 'auto', 'backgroundColor':'lightblue'}}>
                                {Object.entries(this.state.timedEffects).map((timedEffect) => (
                                    <List key = {timedEffect}>
                                        <div style={{"display": "flex", "flexDirection": "row"}}>
                                            &nbsp;
                                            <TextField color="secondary"
                                                size="small"
                                                focused
                                                style = {{width: '50%', color: 'black'}}
                                                InputProps={{
                                                    readOnly: true,
                                                }}
                                                id="outlined"
                                                InputLabelProps={{ style: {zIndex: 0} }}
                                                label="Effect name"
                                                value={timedEffect[1].name}
                                            />&nbsp;&nbsp;

                                            &nbsp;&nbsp;
                                            <TextField color="sixth"
                                                size="small"
                                                focused
                                                style = {{width: '70%', color: 'black'}}
                                                InputProps={{
                                                    readOnly: true,
                                                }}
                                                id="outlined"
                                                InputLabelProps={{ style: {zIndex: 0} }}
                                                label="Effect"
                                                value={timedEffect[1].effect}
                                            />&nbsp;&nbsp;

                                            &nbsp;&nbsp;
                                            <TextField color="sixth"
                                                size="small"
                                                focused
                                                style = {{width: '50%', color: 'black'}}
                                                InputProps={{
                                                    readOnly: true,
                                                }}
                                                id="outlined"
                                                InputLabelProps={{ style: {zIndex: 0} }}
                                                label="Targets / Area"
                                                value={timedEffect[1].targets}
                                            />&nbsp;&nbsp;

                                            &nbsp;&nbsp;
                                            <TextField color="dmblue"
                                                size="small"
                                                focused
                                                style = {{width: '30%', color: 'black'}}
                                                InputProps={{
                                                    readOnly: true,
                                                }}
                                                id="outlined"
                                                InputLabelProps={{ style: {zIndex: 0} }}
                                                label="Rounds left"
                                                value={timedEffect[1].timeLeft}
                                            />&nbsp;&nbsp;
                                            <Button  size="small" variant='contained' color='eighth' style={{"color":"black",  textTransform: "none", fontSize: "10px"}} onClick={() => { 
                                                endEffect(timedEffect[1].name);
                                                this.getTimedEffects();
                                            }} endIcon={<Flare />}>
                                                End Now
                                            </Button>&nbsp;
                                        </div>
                                    </List>
                                ))}
                            </Paper>
                            
                            <h3>Initiative order:</h3>
                            <Paper style={{maxHeight: 500, maxWidth: 1700, overflow: 'auto', 'backgroundColor':'#C5CAE9',  border: '15px solid #C5CAE9', borderRadius: '10px'}}>
                                {Object.entries(this.state.initiativeOrder).map((character) => (
                                    <List key = {character} >
                                        {/* <div>{console.log('character', character)}</div> */}
                                        <div style={{"display": "flex", "flexDirection": "row"}}>
                                            &nbsp;
                                            {this.state.currentTurn == character[1].name ? 
                                                <TextField color="currentturn"
                                                    size="small"
                                                    focused
                                                    style = {{width: '65%'}}
                                                    InputProps={{
                                                        readOnly: true,
                                                        style: { fontSize: 22 },
                                                    }}
                                                    id="outlined"
                                                    InputLabelProps={{ style: { fontSize: 22, fontWeight: 'bold', zIndex: 0} }}
                                                    defaultValue={character[1].name + '  🔍'}
                                                    label="Current turn:"
                                                    onClick={() => {
                                                        this.setState({
                                                            currentCharacterBeingEditedName: character[1].name,
                                                        });
                                                        setTimeout(() => {  
                                                            // need to get CurrentCharacter to view
                                                            this.getCharacterForView();
                                                        }, 10);
                                                    }}
                                                />
                                            : this.state.nextTurn == character[1].name ? 
                                                <TextField color="nextturn"
                                                    size="small"
                                                    focused
                                                    style = {{width: '60%'}}
                                                    InputProps={{
                                                        readOnly: true,
                                                        style: { fontSize: 18 },
                                                    }}
                                                    InputLabelProps={{ style: { fontSize: 18, fontWeight: 'bold', zIndex: 0} }}
                                                    defaultValue={character[1].name + '  🔍'}
                                                    id="outlined"
                                                    label="Next up:"
                                                    onClick={() => {
                                                        this.setState({
                                                            currentCharacterBeingEditedName: character[1].name,
                                                        });
                                                        setTimeout(() => {  
                                                            // need to get CurrentCharacter to view
                                                            this.getCharacterForView();
                                                        }, 10);
                                                    }}
                                                />
                                            : 
                                                <TextField color="secondary"
                                                    size="small"
                                                    focused
                                                    style = {{width: '60%'}}
                                                    InputProps={{
                                                        readOnly: true,
                                                    }}
                                                    id="outlined"
                                                    label="Name"
                                                    InputLabelProps={{ style: {zIndex: 0} }}
                                                    defaultValue={character[1].name + '  🔍'}
                                                    onClick={() => {
                                                        this.setState({
                                                            currentCharacterBeingEditedName: character[1].name,
                                                        });
                                                        setTimeout(() => {  
                                                            // need to get CurrentCharacter to view
                                                            this.getCharacterForView();
                                                        }, 10);
                                                    }}
                                                /> 
                                            }
                                            &nbsp;&nbsp;

                                            

                                            &nbsp;&nbsp;
                                            <TextField color="third"
                                                size="small"
                                                focused
                                                style = {{width: '18%', color: 'black'}}
                                                InputProps={{
                                                    readOnly: true,
                                                }}
                                                id="outlined"
                                                InputLabelProps={{ style: {zIndex: 0} }}
                                                label="Health"
                                                defaultValue={character[1].hp + '  ✎'}
                                                onClick={ () => {
                                                    this.setState({
                                                        currentHealthBeingEditedName: character[1].name,
                                                        modal1Open: true
                                                    });
                                                }}
                                            />&nbsp;&nbsp;

                                            &nbsp;&nbsp;
                                            <TextField color="seventh"
                                                size="small"
                                                focused
                                                style = {{width: '15%', color: 'black'}}
                                                InputProps={{
                                                    readOnly: true,
                                                }}
                                                id="outlined"
                                                type="number"
                                                InputLabelProps={{ style: {fontSize:"14px", zIndex: 0} }}
                                                label="Armor"
                                                defaultValue={character[1].ac}
                                            />&nbsp;&nbsp;

                                            &nbsp;&nbsp;
                                            <TextField color="eighth"
                                                size="small"
                                                focused
                                                style = {{width: '150%', color: 'black'}}
                                                InputProps={{
                                                    readOnly: true,
                                                }}
                                                id="outlined"
                                                InputLabelProps={{ style: {zIndex: 0} }}
                                                label="Main Attacks, Spells, or Abilities"
                                                defaultValue={character[1].attack}
                                            />&nbsp;&nbsp;

                                            &nbsp;&nbsp;
                                            <TextField color="dmblue"
                                                size="small"
                                                focused
                                                style = {{width: '40%', color: 'black'}}
                                                InputProps={{
                                                    readOnly: true,
                                                }}
                                                id="outlined"
                                                InputLabelProps={{ style: {zIndex: 0} }}
                                                label="Resistances"
                                                defaultValue={character[1].resistances}
                                            />&nbsp;&nbsp;

                                            &nbsp;&nbsp;
                                            <TextField color="lightteal"
                                                size="small"
                                                focused
                                                style = {{width: '40%', color: 'black'}}
                                                InputProps={{
                                                    readOnly: true,
                                                }}
                                                id="outlined"
                                                InputLabelProps={{ style: {fontSize:"15px", zIndex: 0} }}
                                                label="Reactions & Weaknesses"
                                                defaultValue={character[1].reactions}
                                            />&nbsp;&nbsp;

                                            &nbsp;&nbsp;
                                            <TextField color="secondary"
                                                size="small"
                                                focused
                                                style = {{width: '11%', color: 'black'}}
                                                InputProps={{
                                                    readOnly: true,
                                                }}
                                                id="outlined"
                                                InputLabelProps={{ style: {fontSize:"12px", zIndex: 0} }}
                                                label="Size"
                                                defaultValue={character[1].size}
                                            />&nbsp;&nbsp;

                                            &nbsp;&nbsp;
                                            <TextField color="speedBlue"
                                                size="small"
                                                focused
                                                style = {{width: '15%', color: 'black'}}
                                                InputProps={{
                                                    readOnly: true,
                                                }}
                                                id="outlined"
                                                InputLabelProps={{ style: {fontSize:"14px", zIndex: 0} }}
                                                label="Speed"
                                                defaultValue={character[1].speed + ' ft'}
                                            />&nbsp;&nbsp;

                                            &nbsp;&nbsp;
                                            <TextField color="editblue"
                                            size="small"
                                            focused
                                            style = {{width: '15%', color: 'black'}}
                                            InputProps={{
                                                readOnly: true,
                                            }}
                                            id="outlined"
                                            type="number"
                                            InputLabelProps={{ style: {fontSize:"12px", zIndex: 0} }}
                                            label="Initiative"
                                            defaultValue={character[1].initTotal}
                                        />&nbsp;

                                            <Button size="small" variant='contained' color='eighth' style={{"color":"black",  textTransform: "none", fontSize: "8px", width: '15%', height:"90%"}} onClick={() => { 
                                                removeFromMapButtonCheck(character[1].name)
                                            }} endIcon={<PersonRemoveAlt1TwoTone />}>
                                                Remove Character
                                            </Button>&nbsp;
                                        </div>
                                    </List>
                                ))}
                            </Paper>
                        </div>
                    </Paper>
                </div>
            </div>
        );
    }

}

export default GamePlayMenu;

function removeFromMapButtonCheck(charName) {
    if (window.confirm('Are you sure you want to remove ' + charName + '?')) {
        removeFromMapV2(charName);
    }
}

var tempCharHealth
function getCharHealth(e) {
    tempCharHealth = e.target.value
    console.log(tempCharHealth)
}

function editCharacterHealth(charName) {
    console.log('editing ' + charName + '\'s health: ' + tempCharHealth)
    apiEditCharacterHealth(charName, tempCharHealth)
}

var tempEffectName
var tempEffect
var tempEffectTargets

var tempEffectRounds = 0
var tempEffectMinutes = 0
var tempEffectHours = 0
var tempEffectDays = 0

function setEffectName(e) {
    tempEffectName = e.target.value
    console.log('effect name:' + tempEffectName)
}

function setEffect(e) {
    tempEffect = e.target.value
    console.log('effect:' + tempEffect)
}

function setEffectTargets(e) {
    tempEffectTargets = e.target.value
    console.log('effect targets:' + tempEffectTargets)
}


function setEffectRounds(e) {
    tempEffectRounds = parseInt(e.target.value, 10)
    console.log('effect round duration:' + tempEffectRounds)
}

function setEffectMinutes(e) {
    tempEffectMinutes = parseInt(e.target.value, 10)
    console.log('effect minute duration:' + tempEffectMinutes)
}

function setEffectHours(e) {
    tempEffectHours = parseInt(e.target.value, 10)
    console.log('effect hour duration:' + tempEffectHours)
}

function setEffectDays(e) {
    tempEffectDays = parseInt(e.target.value, 10)
    console.log('effect day duration:' + tempEffectDays)
}

function timedEffectCreateButton() {
    if (tempEffectName == null) {
        alert('Some required fields are blank')
    } else {
        var tempEffectDuration = tempEffectRounds + tempEffectMinutes*10 + tempEffectHours*600 + tempEffectDays*14400
        console.log('total duration rounds:' + tempEffectDuration)
        if (tempEffectDuration <= 0) {
            alert('Invalid duration')
        } else {
            addEffect(tempEffectName, tempEffect, tempEffectTargets, tempEffectDuration)
        }
    }
}

// API functions:

async function apiEditCharacterHealth(charName, currentHealth) {
    
    const res = await fetch(window.apiURL + '/playermenu/editcharacterhealth', {
    method: 'POST',
    body: JSON.stringify({
        "characterName": charName,
        "currentHealth": currentHealth,
    })})
    .then((response) => {
        if(response.status === 400){
            console.log('Character', charName, 'does not exist');
        } else if (response.status === 205) {
            if (window.confirm(charName + '\'s health is now less than 1. Press \'OK\' to remove them.')) {
                removeFromMapV2(charName)
            } else {
                console.log('Edited character', charName, '\'s health');
                window.location.reload(false);
            }
        } else if (response.status === 200) {
            console.log('Edited character', charName, '\'s health');
            window.location.reload(false);
        }
    })
    .catch(err => {
        console.log(err);
    });
    
}

async function addEffect(effectName, effect, effectTargets, effectDuration) {
    const res = await fetch(window.apiURL + '/playermenu/addtimedeffect', {
    method: 'POST',
    body: JSON.stringify({
        "name": effectName,
        "effect": effect,
        "targets": effectTargets,
        "durationRounds": effectDuration,
    })})
    .catch(err => {
        console.log(err);
    });
}

async function endEffect(effectName) {
    const res = await fetch(window.apiURL + '/playermenu/removetimedeffect', {
    method: 'POST',
    body: effectName,
    })
    .catch(err => {
        console.log(err);
    });
}
