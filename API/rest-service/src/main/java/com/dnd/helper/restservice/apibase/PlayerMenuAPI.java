package com.dnd.helper.restservice.apibase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dnd.helper.restservice.gamedata.DndCharacter;
import com.dnd.helper.restservice.gamedata.GameData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/playermenu")
@CrossOrigin(origins = "*")
public class PlayerMenuAPI {
	private GameData gameData;
	
	final String openBracket = "%7B";
    final String closedBracket = "%7D";
    final String quotation = "%22";
    final String colon = "%3A";
    final String comma = "%2C";
    final String space = "\\+";
    final String equals = "=";
    final String apostrophe = "%27";
    final String plusSign = "%2B";
    final String backSlash ="%5C";
    final String forwardSlash ="%2F";
    final String xmlCheck = "%7B%22";
	
	public PlayerMenuAPI(){
		gameData = new GameData();
	}
	
	@PostMapping(value = "/addcharacter")
	//	{
	//	"characterName":"",
	//	"maxHealth":"",
	//	"currentHealth":""," -> optional?
	//	"initiative":"",
	//	"armorClass":"",
	//	"touch":"",
	//	"flatFooted":"",
	// 	"fortSave":"",
	// 	"refSave":"",
	// 	"willSave":"",
	// 	"grapple":"",
	// 	"speed":"",
	// 	"attacks":"",
	// 	"skills":"",
	// 	"spells":"",
	// 	"magicItems":"",
	// 	"loot":"",
	// 	"feats":"",
	// 	"other":"",
	//	"race":"",
	//	"size":"",
	//	"reactions":"",
	//	"resistances":"",
	//	"otherSpells":"",
	//	"otherAbilities":"",
	//	"stats":"",
	//  "isNPC":"false",
	//}
	public ResponseEntity<String> addCharacter(@RequestBody String tempChar) {
		try {
            JSONObject charJson = new JSONObject(tempChar);
            String name = (String) charJson.get("characterName");
            
			if (getAllCharacterNames() != null) {
				if (getAllCharacterNames().contains(name)) {
					System.out.println("Character " + name + " already exists.");
					return new ResponseEntity<> ("Character " + name + " already exists.", HttpStatus.BAD_REQUEST); // 400
				}
			}
           
        	int maxHealth = Integer.parseInt( (String) charJson.get("maxHealth") );
        	int initiative = Integer.parseInt(  (String) charJson.get("initiative") );
        	
        	int ac = Integer.parseInt(  (String) charJson.get("armorClass") );
        	int touch = Integer.parseInt(  (String) charJson.get("touch") );
        	int flatFooted = Integer.parseInt(  (String) charJson.get("flatFooted") );
        	
        	int fortSaveTotal = Integer.parseInt( (String) charJson.get("fortSave") );
        	int refSaveTotal = Integer.parseInt( (String) charJson.get("refSave") );
        	int willSaveTotal = Integer.parseInt( (String) charJson.get("willSave") );
        	
        	int grapple = Integer.parseInt( (String) charJson.get("grapple") );
        	int speed = Integer.parseInt( (String) charJson.get("speed") );
        	
        	String attacks = (String) charJson.get("attacks");
        	
        	boolean isNPC = Boolean.parseBoolean((String) charJson.get("isNPC"));
        	
        	// required values ^
        	// optional values:
        	String race = "Human";
        	String size = "M";
        	String skills = "None";
        	String spells = "None";
        	String status = "Normal";
        	String feats = "None";
        	String magicItems = "None";
        	String loot = "None";
        	String other = "Likes ducks";
        	String resistances = "None";
        	String reactions = "None";
        	String otherSpells = "None";
        	String otherAbilities = "None";
        	String stats = "Str 10 Dex 10 Con 10 Int 10 Wis 10 Cha 10";
 
        	try {
        		race = (String) charJson.get("race");
            } catch (JSONException e) {
            	System.out.println("Defaulting race to Human (Ignored exception = "+e.toString() +")");
            }
        	
        	try {
        		skills = (String) charJson.get("skills");
            } catch (JSONException e) {
            	System.out.println("Defaulting skills to None (Ignored exception = "+e.toString() +")");
            }
        	
        	try {
        		spells = (String) charJson.get("spells");
            } catch (JSONException e) {
            	System.out.println("Defaulting spells to None (Ignored exception = "+e.toString() +")");
            }
        	
        	try {
        		magicItems = (String) charJson.get("magicItems");
            } catch (JSONException e) {
            	System.out.println("Defaulting magicItems to None (Ignored exception = "+e.toString() +")");
            }

        	try {
        		loot = (String) charJson.get("loot");
            } catch (JSONException e) {
            	System.out.println("Defaulting loot to None (Ignored exception = "+e.toString() +")");
            }

        	try {
        		feats = (String) charJson.get("feats");
            } catch (JSONException e) {
            	System.out.println("Defaulting feats to None (Ignored exception = "+e.toString() +")");
            }
        	
        	try {
        		status = (String) charJson.get("status");
            } catch (JSONException e) {
            	System.out.println("Defaulting status to Normal (Ignored exception = "+e.toString() +")");
            }
			
        	try {
        		size = (String) charJson.get("size");
            } catch (JSONException e) {
            	System.out.println("Defaulting size to M (Ignored exception = "+e.toString() +")");
            }
        	
        	try {
        		other = (String) charJson.get("other");
            } catch (JSONException e) {
            	System.out.println("Defaulting other to Likes ducks (Ignored exception = "+e.toString() +")");
            }
        	
        	try {
        		reactions = (String) charJson.get("reactions");
            } catch (JSONException e) {
            	System.out.println("Defaulting reactions to None (Ignored exception = "+e.toString() +")");
            }
        	
        	try {
        		resistances = (String) charJson.get("resistances");
            } catch (JSONException e) {
            	System.out.println("Defaulting resistances to None (Ignored exception = "+e.toString() +")");
            }
        	
        	try {
        		otherAbilities = (String) charJson.get("otherAbilities");
            } catch (JSONException e) {
            	System.out.println("Defaulting otherAbilities to None (Ignored exception = "+e.toString() +")");
            }
        	
        	try {
        		otherSpells = (String) charJson.get("moreSpells");
            } catch (JSONException e) {
            	System.out.println("Defaulting otherSpells to None (Ignored exception = "+e.toString() +")");
            }
        	
        	try {
        		stats = (String) charJson.get("stats");
            } catch (JSONException e) {
            	System.out.println("Defaulting stats to " + stats + " (Ignored exception = "+e.toString() +")");
            }
             
             DndCharacter dndChar = gameData.createBaseCharacter(name, maxHealth, initiative, ac, touch, flatFooted, race, size, isNPC, 
            		 fortSaveTotal, refSaveTotal, willSaveTotal, grapple, speed, attacks, skills, spells, magicItems, loot, feats, status, 
            		 other, reactions, resistances, otherSpells, otherAbilities, stats);
             
             try {
	             String currentHealth = (String) charJson.get("currentHealth");
	             if(StringUtils.hasText(currentHealth)) {
	            	 dndChar.setCurrentHealth(Integer.parseInt(currentHealth));
	             }
             } catch (JSONException e) {
            	 System.out.println("Current Health was null, ignoring... (Exception: "+e.toString() +")");
             }
        	
        	addCharacterToGame(dndChar, ""); // "" = add to not in map
        	
        	return new ResponseEntity<> (new ObjectMapper().writeValueAsString(getAllCharacterNames()), HttpStatus.OK); // 200
        	
            
        } catch (JSONException err) { // client side formatting error 
            System.out.println("Exception : "+err.toString());
            return new ResponseEntity<> (err.toString(), HttpStatus.BAD_REQUEST);
        } catch (JsonProcessingException err) { // server side error processing Map object
	        System.out.println("Exception : "+err.toString());
	        return new ResponseEntity<> (err.toString(), HttpStatus.INTERNAL_SERVER_ERROR); // 500
	    } 
		
	}
	
	@PostMapping(value = "/editcharacter")
	//	{
	//	"characterName":"",
	//	"maxHealth":"",
	//	"currentHealth":""," -> optional?
	//	"initiative":"",
	//	"armorClass":"",
	//	"touch":"",
	//	"flatFooted":"",
	// 	"fortSave":"",
	// 	"refSave":"",
	// 	"willSave":"",
	// 	"grapple":"",
	// 	"speed":"",
	// 	"attacks":"",
	// 	"skills":"",
	// 	"spells":"",
	// 	"magicItems":"",
	// 	"loot":"",
	// 	"feats":"",
	// 	"other":"",
	//	"race":"",
	//	"size":"",
	//	"reactions":"",
	//	"resistances":"",
	//	"otherSpells":"",
	//	"otherAbilities":"",
	//	"stats":"",
	//  "isNPC":"false",
	//}
	public ResponseEntity<String> editCharacter(@RequestBody String tempChar) {
		try {
            JSONObject charJson = new JSONObject(tempChar);
            String name = (String) charJson.get("characterName");
            
			if (getAllCharacterNames() != null) {
				if (!getAllCharacterNames().contains(name)) {
					System.out.println("Character " + name + " does not exist, creating...");
					return addCharacter(tempChar);
				}
			} else {
				return new ResponseEntity<> ("No characters found", HttpStatus.INTERNAL_SERVER_ERROR); // 500
			}
			
			System.out.println("Editing " + name + "...");
           
        	int maxHealth = Integer.parseInt( (String) charJson.get("maxHealth") );
        	int initiative = Integer.parseInt(  (String) charJson.get("initiative") );
        	
        	int ac = Integer.parseInt(  (String) charJson.get("armorClass") );
        	int touch = Integer.parseInt(  (String) charJson.get("touch") );
        	int flatFooted = Integer.parseInt(  (String) charJson.get("flatFooted") );
        	
        	int fortSaveTotal = Integer.parseInt( (String) charJson.get("fortSave") );
        	int refSaveTotal = Integer.parseInt( (String) charJson.get("refSave") );
        	int willSaveTotal = Integer.parseInt( (String) charJson.get("willSave") );
        	
        	int grapple = Integer.parseInt( (String) charJson.get("grapple") );
        	int speed = Integer.parseInt( (String) charJson.get("speed") );
        	
        	String attacks = (String) charJson.get("attacks");
        	
        	boolean isNPC = Boolean.parseBoolean((String) charJson.get("isNPC"));
        	
        	// required values ^
        	// optional values:
        	String race = "Human";
        	String size = "M";
        	String skills = "None";
        	String spells = "None";
        	String status = "Normal";
        	String feats = "None";
        	String magicItems = "None";
        	String loot = "None";
        	String other = "Likes ducks";
        	int currentHealth = maxHealth;
        	String resistances = "None";
        	String reactions = "None";
        	String otherSpells = "None";
        	String otherAbilities = "None";
        	String stats = "Str 10 Dex 10 Con 10 Int 10 Wis 10 Cha 10";
 
        	try {
        		race = (String) charJson.get("race");
            } catch (JSONException e) {
            	System.out.println("Defaulting race to Human (Ignored exception = "+e.toString() +")");
            }
        	
        	try {
        		skills = (String) charJson.get("skills");
            } catch (JSONException e) {
            	System.out.println("Defaulting skills to None (Ignored exception = "+e.toString() +")");
            }
        	
        	try {
        		spells = (String) charJson.get("spells");
            } catch (JSONException e) {
            	System.out.println("Defaulting spells to None (Ignored exception = "+e.toString() +")");
            }
        	
        	try {
        		magicItems = (String) charJson.get("magicItems");
            } catch (JSONException e) {
            	System.out.println("Defaulting magicItems to None (Ignored exception = "+e.toString() +")");
            }

        	try {
        		loot = (String) charJson.get("loot");
            } catch (JSONException e) {
            	System.out.println("Defaulting loot to None (Ignored exception = "+e.toString() +")");
            }

        	try {
        		feats = (String) charJson.get("feats");
            } catch (JSONException e) {
            	System.out.println("Defaulting feats to None (Ignored exception = "+e.toString() +")");
            }
        	
        	try {
        		status = (String) charJson.get("status");
            } catch (JSONException e) {
            	System.out.println("Defaulting status to Normal (Ignored exception = "+e.toString() +")");
            }
			
        	try {
        		size = (String) charJson.get("size");
            } catch (JSONException e) {
            	System.out.println("Defaulting size to M (Ignored exception = "+e.toString() +")");
            }
        	
        	try {
        		other = (String) charJson.get("other");
            } catch (JSONException e) {
            	System.out.println("Defaulting other to Likes ducks (Ignored exception = "+e.toString() +")");
            }
        	
        	try {
        		reactions = (String) charJson.get("reactions");
            } catch (JSONException e) {
            	System.out.println("Defaulting reactions to None (Ignored exception = "+e.toString() +")");
            }
        	
        	try {
        		resistances = (String) charJson.get("resistances");
            } catch (JSONException e) {
            	System.out.println("Defaulting resistances to None (Ignored exception = "+e.toString() +")");
            }
        	
        	try {
        		otherAbilities = (String) charJson.get("otherAbilities");
            } catch (JSONException e) {
            	System.out.println("Defaulting otherAbilities to None (Ignored exception = "+e.toString() +")");
            }
        	
        	try {
        		otherSpells = (String) charJson.get("moreSpells");
            } catch (JSONException e) {
            	System.out.println("Defaulting otherSpells to None (Ignored exception = "+e.toString() +")");
            }
        	
        	try {
        		stats = (String) charJson.get("stats");
            } catch (JSONException e) {
            	System.out.println("Defaulting stats to " + stats + " (Ignored exception = "+e.toString() +")");
            }
        	
        	try {
	             currentHealth = Integer.parseInt((String) charJson.get("currentHealth"));
            } catch (JSONException e) {
            	System.out.println("Defaulting currentHealth to maxHealth (Ignored exception = "+e.toString() +")");
            }
             
             if (gameData.editCharacterData(name, maxHealth, currentHealth, initiative, ac, touch, flatFooted, race, size, isNPC, 
            		 fortSaveTotal, refSaveTotal, willSaveTotal, grapple, speed, attacks, skills, spells, magicItems, loot, feats, 
            		 status, other, reactions, resistances, otherSpells, otherAbilities, stats)) {
            	 return new ResponseEntity<> (new ObjectMapper().writeValueAsString(getAllCharacterNames()), HttpStatus.OK); // 200
             } else {
            	 return new ResponseEntity<> ("Character " + name + " does not exist.", HttpStatus.BAD_REQUEST);
             }
        } catch (JSONException err) { // client side formatting error 
            System.out.println("Exception : "+err.toString());
            return new ResponseEntity<> (err.toString(), HttpStatus.BAD_REQUEST);
        } catch (JsonProcessingException err) { // server side error processing Map object
	        System.out.println("Exception : "+err.toString());
	        return new ResponseEntity<> (err.toString(), HttpStatus.INTERNAL_SERVER_ERROR); // 500
	    } 
		
	}
	
	// this now takes in a value for damage taken. return a 205 on a "dead" character
	@PostMapping(value = "/editcharacterhealth")
	//	{
	//	"characterName":"",
	//	"currentHealth":"",
	//}
	public ResponseEntity<String> editCharacterHealth(@RequestBody String tempChar) {
		try {
            JSONObject charJson = new JSONObject(tempChar);
            String name = (String) charJson.get("characterName");
            
			if (getAllCharacterNames() != null) {
				if (!getAllCharacterNames().contains(name)) {
					System.out.println("Character " + name + " does not exist.");
					return new ResponseEntity<> ("Character " + name + " does not exist.", HttpStatus.BAD_REQUEST); // 400
				}
			} else {
				return new ResponseEntity<> ("No characters found", HttpStatus.INTERNAL_SERVER_ERROR); // 500
			}
			
			System.out.println("Editing " + name + "\'s health...");
			int currentHealth = Integer.parseInt((String) charJson.get("currentHealth"));
			if (!gameData.updateCharacterCurrentHealth(name, currentHealth)) {
				// if here, then the new health is <= 0
				return new ResponseEntity<> (new ObjectMapper().writeValueAsString("Updated " + name), HttpStatus.RESET_CONTENT); // 205
			}
			
			return new ResponseEntity<> (new ObjectMapper().writeValueAsString("Updated " + name), HttpStatus.OK); // 200
		} catch (JsonProcessingException err) { // server side error processing Map object
	        System.out.println("Exception : "+err.toString());
	        return new ResponseEntity<> (err.toString(), HttpStatus.INTERNAL_SERVER_ERROR); // 500
	    } 
		
	}
	
	@PostMapping(value = "/duplicatecharacter")
	//	{
	//	"characterName":"",
	//}
	public ResponseEntity<String> duplicateCharacterEndpoint(@RequestBody String tempChar) {
		try {
            JSONObject charJson = new JSONObject(tempChar);
            String name = (String) charJson.get("characterName");
            int numberOfCopies = Integer.parseInt(  (String) charJson.get("numberOfCopies") );
            
			if (getAllCharacterNames() != null) {
				if (!getAllCharacterNames().contains(name)) {
					System.out.println("Character " + name + " does not exist.");
					return new ResponseEntity<> ("Character " + name + " does not exist.", HttpStatus.BAD_REQUEST); // 400
				}
			} else {
				return new ResponseEntity<> ("No characters found", HttpStatus.INTERNAL_SERVER_ERROR); // 500
			}
			
			gameData.duplicateCharacter(name, numberOfCopies);
           
			return new ResponseEntity<> ("Character " + name + " duplicated.", HttpStatus.OK); // 200

        } catch (JSONException err) { // client side formatting error 
            System.out.println("Exception : "+err.toString());
            return new ResponseEntity<> (err.toString(), HttpStatus.BAD_REQUEST);
        }
		
	}

	@GetMapping(value = "/getcharacter/{characterName}")
	public ResponseEntity<String> getCharacterEndpoint(@PathVariable String characterName) {
		try {
			DndCharacter tempD = gameData.getCharacterInAnyMap(characterName);
			if (tempD != null) {
				return new ResponseEntity<> (new ObjectMapper().writeValueAsString(tempD), HttpStatus.OK); // 200
			} else {
				return new ResponseEntity<> ("Character " + characterName + " does not exist.", HttpStatus.BAD_REQUEST);
			}
		} catch (JsonProcessingException err) {
	        System.out.println("Exception : "+err.toString());
	        return new ResponseEntity<> (err.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
	    } 
	}
	
	@PostMapping(value = "/removecharacter")
//	{
//		"characterName":""
//	}
	public ResponseEntity<String> removeCharacter(@RequestBody String characterName) {
		try {
			Vector<DndCharacter> inMapChars = getCharacters(true);
			Vector<DndCharacter> notInMapChars = getCharacters(false);
			
			if (inMapChars != null) {
				for (DndCharacter d: inMapChars) { // in map
					if (Objects.equals(d.getCharName(), characterName)) {
						inMapChars.remove(d);
						System.out.println("Removed character " + characterName);
						return new ResponseEntity<> (new ObjectMapper().writeValueAsString(getAllCharacterNames()), HttpStatus.OK); // 200
					}
				}
			}
			if (notInMapChars != null) {
				for (DndCharacter d : notInMapChars) { // not in map
					if (Objects.equals(d.getCharName(), characterName)) {
						notInMapChars.remove(d);
						System.out.println("Removed character " + characterName);
						return new ResponseEntity<> (new ObjectMapper().writeValueAsString(getAllCharacterNames()), HttpStatus.OK); // 200
					}
				}
			}
			return new ResponseEntity<> ("Character " + characterName + " does not exist.", HttpStatus.BAD_REQUEST);
		} catch (JSONException err) { // client side formatting error 
	        System.out.println("Exception : "+err.toString());
	        return new ResponseEntity<> (err.toString(), HttpStatus.BAD_REQUEST);
	    } catch (JsonProcessingException err) { // server side error processing Map object
	        System.out.println("Exception : "+err.toString());
	        return new ResponseEntity<> (err.toString(), HttpStatus.INTERNAL_SERVER_ERROR); // 500
	    }
	}
	
	@GetMapping(value = "/getcharacters/{inMap}")
	public ResponseEntity<String> getCharsactersAPI(@PathVariable boolean inMap) {
		try {
			return new ResponseEntity<> (new ObjectMapper().writeValueAsString(getCharacterNames(inMap)), HttpStatus.OK);
		} catch (JsonProcessingException err) {
	        System.out.println("Exception : "+err.toString());
	        return new ResponseEntity<> (err.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
	    } 
	}
	
	@GetMapping(value = "/getpcs")
	public ResponseEntity<String> getPCsEndpoint() {
		try {
			return new ResponseEntity<> (new ObjectMapper().writeValueAsString(getPCsInMap()), HttpStatus.OK);
		} catch (JsonProcessingException err) {
	        System.out.println("Exception : "+err.toString());
	        return new ResponseEntity<> (err.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
	    } 
	}
	
	@GetMapping(value = "/getallpcs")
	public ResponseEntity<String> getAllPCsEndpoint() {
		try {
			return new ResponseEntity<> (new ObjectMapper().writeValueAsString(getPCs()), HttpStatus.OK);
		} catch (JsonProcessingException err) {
	        System.out.println("Exception : "+err.toString());
	        return new ResponseEntity<> (err.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
	    } 
	}
	
	@PostMapping(value = "/movetomap")
//	{
//		"characterName":""
//	}
	public ResponseEntity<String> moveCharacterToMap(@RequestBody String characterName) {
		try {
			characterName = removeTrailingEquals(characterName);
			
			Vector<DndCharacter> inMapChars = getCharacters(true);
			Vector<DndCharacter> notInMapChars = getCharacters(false);
			
			if (notInMapChars != null) {
				for (DndCharacter d : notInMapChars) { // not in map
					if (Objects.equals(d.getCharName(), characterName)) {
						if (inMapChars != null) {
							inMapChars.add(d);
							notInMapChars.remove(d);
						}
						gameData.addNewCharacterToInitMapUnrolled(d); // setting initiative area 2
						System.out.println("Moved character " + characterName + " into the map.");
						return new ResponseEntity<> (new ObjectMapper().writeValueAsString(getAllCharacterNames()), HttpStatus.OK); // 200
					}
				}
				return new ResponseEntity<> ("Character " + characterName + " is not not in the map.", HttpStatus.BAD_REQUEST);
			}
		
			return new ResponseEntity<> ("Character " + characterName + " loading error.", HttpStatus.BAD_REQUEST);
		} catch (JSONException err) { // client side formatting error 
	        System.out.println("Exception : "+err.toString());
	        return new ResponseEntity<> (err.toString(), HttpStatus.BAD_REQUEST);
	    } catch (JsonProcessingException err) { // server side error processing Map object
	        System.out.println("Exception : "+err.toString());
	        return new ResponseEntity<> (err.toString(), HttpStatus.INTERNAL_SERVER_ERROR); // 500
	    }
	}
	
	@PostMapping(value = "/removefrommap")
//	{
//		"characterName"
//	}
	public ResponseEntity<String> removeCharacterFromMap(@RequestBody String characterName) {
		try {
			Vector<DndCharacter> inMapChars = getCharacters(true);
			Vector<DndCharacter> notInMapChars = getCharacters(false);
			
			
			if (inMapChars != null) {
				for (DndCharacter d : inMapChars) { // not in map
					if (Objects.equals(d.getCharName(), characterName)) {
						if (notInMapChars != null) {
							notInMapChars.add(d);
							inMapChars.remove(d);
						}
						
						// update next character if already next character
						if (gameData.getNextCharacterName() != null && gameData.getNextCharacterName().equals(d.getCharName())) {
							gameData.findNextCharacterOnRemove();
						}
						
						gameData.removeCharacterFromInitMapUnrolled(d);
						
						System.out.println("Moved character " + characterName + " out of the map.");
						return new ResponseEntity<> (new ObjectMapper().writeValueAsString(getAllCharacterNames()), HttpStatus.OK); // 200
					}
				}
				return new ResponseEntity<> ("Character " + characterName + " is not in the map.", HttpStatus.BAD_REQUEST);
			}
		
			return new ResponseEntity<> ("Character " + characterName + " loading error.", HttpStatus.BAD_REQUEST);
		} catch (JSONException err) { // client side formatting error 
	        System.out.println("Exception : "+err.toString());
	        return new ResponseEntity<> (err.toString(), HttpStatus.BAD_REQUEST);
	    } catch (JsonProcessingException err) { // server side error processing Map object
	        System.out.println("Exception : "+err.toString());
	        return new ResponseEntity<> (err.toString(), HttpStatus.INTERNAL_SERVER_ERROR); // 500
	    }
	}
	
	@PostMapping(value = "/rollinitiative") // list of totals from PCs
//	{
//	  "jim":"10",
//	  "john":"11",
//  }
	public ResponseEntity<String> rollInitiativeEndpoint(@RequestBody String pcInitiatives) {
		Map<DndCharacter, Integer> pcInitMap = new HashMap<>();

		try {
			pcInitiatives = parseXML(pcInitiatives);
			
			JSONObject charJson = new JSONObject(pcInitiatives);
			
			for (DndCharacter d : getCharacters(true)) {
				if (!d.isNpc()) { // if the character is a PC
					int initRoll = Integer.valueOf( (String) charJson.get(d.getCharName()));	
					pcInitMap.put(d, initRoll);
				}
			}
			
			Vector<Map<String, String>> initNames = gameData.rollInitiative(pcInitMap);
					
			return new ResponseEntity<> (new ObjectMapper().writeValueAsString(initNames), HttpStatus.OK);
		} catch (JSONException err) { // client side formatting error 
	        System.out.println("Exception : "+err.toString());
	        return new ResponseEntity<> (err.toString(), HttpStatus.BAD_REQUEST);
	    } catch (JsonProcessingException err) { // server side error processing Map object
	        System.out.println("Exception : "+err.toString());
	        return new ResponseEntity<> (err.toString(), HttpStatus.INTERNAL_SERVER_ERROR); // 500
	    }		
	}
	
	public String parseXML(String str) {
		if (firstSix(str).equals(xmlCheck)) {
			String parsedStr = "";
//			System.out.println("XML string: " + str);
			str = str.replaceAll(openBracket, "{");
			str = str.replaceAll(closedBracket, "}");
			str = str.replaceAll(quotation, "\"");
			str = str.replaceAll(colon, ":");
			str = str.replaceAll(comma, ",");
			str = str.replaceAll(space, " ");
			str = str.replaceAll(apostrophe, "\'");
			str = str.replaceAll(plusSign, "+");
			str = str.replaceAll(backSlash, "/");
			str = str.replaceAll(forwardSlash, "/");
			parsedStr = removeTrailingEquals(str);
//			System.out.println("parsedStr: " + parsedStr);
			return parsedStr;
		}
		return str;
	}
	
	public String firstSix(String str) {
	    return str.length() < 6 ? str : str.substring(0, 6);
	}
	
	public String removeTrailingEquals(String str) {
		if (str.substring(str.length() - 1).equals(equals)) {
			str = str.substring(0, str.length() - 1);
//			System.out.println("parsedStr: " + str);
		}
		return str;
	}
	
	@GetMapping(value = "/getinitiative")
	public ResponseEntity<String> getInitiativeEndpoint() {
		try {
			return new ResponseEntity<> (new ObjectMapper().writeValueAsString(gameData.getInitiativeOrder()), HttpStatus.OK);
	    } catch (JsonProcessingException err) { // server side error processing Map object
	        System.out.println("Exception : "+err.toString());
	        return new ResponseEntity<> (err.toString(), HttpStatus.INTERNAL_SERVER_ERROR); // 500
	    }		
	}
	
	@PostMapping(value = "/changeinitiative") // list of totals from PCs/NPCs
//	{
//	  "jim":"10",
//	  "john":"11",
//  }
	public ResponseEntity<String> changeInitiativeEndpoint(@RequestBody String newInitiatives) {
		
		try {
			JSONObject initJson = new JSONObject(newInitiatives);
			Iterator<String> keys = initJson.keys();
			
			while(keys.hasNext()) {
			    String key = keys.next();
//			    System.out.println("character: " + key); 
			    if (getAllCharacterNames().contains(key)) {
		    		int tempNewInit = Integer.valueOf( (String) initJson.get(key) );
//		    		System.out.println("tempNewInit: " + tempNewInit);
		    		gameData.updateSingleCharacterInitiative(key, tempNewInit);
		    	}
			}
			
			gameData.changeInitiativeOrder();
			
			return new ResponseEntity<> (new ObjectMapper().writeValueAsString(gameData.getInitiativeOrder()), HttpStatus.OK);
//			gameData.setInitiativeOrder(initNames);
		} catch (JSONException err) { // client side formatting error 
	        System.out.println("Exception : "+err.toString());
	        return new ResponseEntity<> (err.toString(), HttpStatus.BAD_REQUEST);
	    } catch (JsonProcessingException err) { // server side error processing Map object
	        System.out.println("Exception : "+err.toString());
	        return new ResponseEntity<> (err.toString(), HttpStatus.INTERNAL_SERVER_ERROR); // 500
	    }		
	}
	
	
	// timed effects
	
	@GetMapping(value="/gettimedeffects")
	public ResponseEntity<String> getTimedEffectsEndpoint() {
		try {
			return new ResponseEntity<> (new ObjectMapper().writeValueAsString(gameData.getDurationEffects()), HttpStatus.OK);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new ResponseEntity<> (e.toString(), HttpStatus.INTERNAL_SERVER_ERROR); // 500
		}
	}
	
	@PostMapping(value="/addtimedeffect")
//	{
//	    "name":"bear strength",
//	    "effect":"+4 strength",
//	    "targets": "john,jimp",
//	    "durationRounds":"1",
//	}
	public ResponseEntity<String> addTimedEffectEndpoint(@RequestBody String timedEffect) {
		try {
			timedEffect = parseXML(timedEffect);
//			System.out.println("timed effect: " + timedEffect);
			
			String effect = "something is happening";
			String effectTargets = "everywhere";
			
			JSONObject effectJson = new JSONObject(timedEffect);
			String effectName = (String) effectJson.get("name");
			long effectDuration = (Integer) effectJson.get("durationRounds");

			try {
				effect = (String) effectJson.get("effect");
			} catch (Exception e) {
				System.out.println("invald effect, defaulting. Ignored exception: " + e.getMessage());
			}
			
			try {
				effectTargets = (String) effectJson.get("targets");
			} catch (Exception e) {
				System.out.println("invalid effectTargets, defaulting. Ignored exception: " + e.getMessage());

			}

			if (gameData.addDurationEffect(effectName, effect, effectTargets, effectDuration)) {
				return new ResponseEntity<> (new ObjectMapper().writeValueAsString(gameData.getDurationEffects()), HttpStatus.OK);
			} else {
				return new ResponseEntity<> ("Effect " + effectName + " already exists", HttpStatus.BAD_REQUEST);
			}
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new ResponseEntity<> (e.toString(), HttpStatus.INTERNAL_SERVER_ERROR); // 500
		}
	}
	
	@PostMapping(value="/removetimedeffect")
//	{
//	    "bear strength"
//	}
	public ResponseEntity<String> endTimedEffectEndpoint(@RequestBody String timedEffect) {
		try {
			if (gameData.removeDurationEffect(timedEffect)) {
				return new ResponseEntity<> (new ObjectMapper().writeValueAsString(gameData.getDurationEffects()), HttpStatus.OK);
			} else {
				return new ResponseEntity<> ("Effect " + timedEffect + " does not exist", HttpStatus.BAD_REQUEST);
			}
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new ResponseEntity<> (e.toString(), HttpStatus.INTERNAL_SERVER_ERROR); // 500
		}
	}
	
	// turns:
	
	@GetMapping(value="/gettimestring")
	public ResponseEntity<String> getTimeStrEndpoint() {
		return new ResponseEntity<> (gameData.getTimeString(), HttpStatus.OK);
	}
	
	@GetMapping(value="/setupturns")
	public ResponseEntity<String> setupTurnsEndpoint() {
		
		if (gameData.setInitialTurns()) {
			return new ResponseEntity<> (gameData.getTimeString(), HttpStatus.OK);
		} else {
			return new ResponseEntity<> ("Error going to next turn", HttpStatus.INTERNAL_SERVER_ERROR); // 500
		}	
	}
	
	@GetMapping(value="/nextturn")
	public ResponseEntity<String> nextTurnEndpoint() {
		
		if (gameData.nextTurn()) {
			return new ResponseEntity<> (gameData.getTimeString(), HttpStatus.OK);
		} else {
			return new ResponseEntity<> ("Error going to next turn", HttpStatus.INTERNAL_SERVER_ERROR); // 500
		}	
	}
	
	@GetMapping(value="/getcurrentcharacter")
	public ResponseEntity<String> getCurrentCharacterEndpoint() {
		return new ResponseEntity<> (gameData.getCurrentCharacterName(), HttpStatus.OK);
	}
	
	@GetMapping(value="/getnextcharacter")
	public ResponseEntity<String> getNextCharacterEndpoint() {
		return new ResponseEntity<> (gameData.getNextCharacterName(), HttpStatus.OK);
	}
	
	@GetMapping(value="/resettime")
	public ResponseEntity<String> resetTimeEndpoint() {
		gameData.resetTime();
		return new ResponseEntity<> (gameData.getTimeString(), HttpStatus.OK);
	}
	
	@PostMapping(value="/addrounds")
//	{
//		"0"
//	}
	public ResponseEntity<String> addRoundsEndpoint(@RequestBody String rounds) {
		gameData.addRounds(Long.valueOf(rounds));
		return new ResponseEntity<> (gameData.getTimeString(), HttpStatus.OK);
	}
	
	@PostMapping(value="/subtractrounds")
//	{
//		"0"
//	}
	public ResponseEntity<String> subtractRoundsEndpoint(@RequestBody String rounds) {
		gameData.subtractRounds(Long.valueOf(rounds));
		return new ResponseEntity<> (gameData.getTimeString(), HttpStatus.OK);
	}
	
	@PostMapping(value="/addminutes")
//	{
//		"0"
//	}
	public ResponseEntity<String> addMinutesEndpoint(@RequestBody String minutes) {
		gameData.addMinutes(Long.valueOf(minutes));
		return new ResponseEntity<> (gameData.getTimeString(), HttpStatus.OK);
	}
	
	@PostMapping(value="/subtractminutes")
//	{
//		"0"
//	}
	public ResponseEntity<String> subtractMinutesEndpoint(@RequestBody String minutes) {
		gameData.subtractMinutes(Long.valueOf(minutes));
		return new ResponseEntity<> (gameData.getTimeString(), HttpStatus.OK);
	}
	
	@PostMapping(value="/addhours")
//	{
//		"0"
//	}
	public ResponseEntity<String> addHoursEndpoint(@RequestBody String hours) {
		gameData.addHours(Long.valueOf(hours));
		return new ResponseEntity<> (gameData.getTimeString(), HttpStatus.OK);
	}
	
	@PostMapping(value="/subtracthours")
//	{
//		"0"
//	}
	public ResponseEntity<String> subtractHoursEndpoint(@RequestBody String hours) {
		gameData.subtractHours(Long.valueOf(hours));
		return new ResponseEntity<> (gameData.getTimeString(), HttpStatus.OK);
	}
	
	@PostMapping(value="/adddays")
//	{
//		"0"
//	}
	public ResponseEntity<String> addDaysEndpoint(@RequestBody String days) {
		gameData.addDays(Long.valueOf(days));
		return new ResponseEntity<> (gameData.getTimeString(), HttpStatus.OK);
	}
	
	@PostMapping(value="/subtractdays")
//	{
//		"0"
//	}
	public ResponseEntity<String> subtractDaysEndpoint(@RequestBody String days) {
		gameData.subtractDays(Long.valueOf(days));
		return new ResponseEntity<> (gameData.getTimeString(), HttpStatus.OK);
	}
	
	@PostMapping(value="/addyears")
//	{
//		"0"
//	}
	public ResponseEntity<String> addYearsEndpoint(@RequestBody String years) {
		gameData.addYears(Long.valueOf(years));
		return new ResponseEntity<> (gameData.getTimeString(), HttpStatus.OK);
	}
	
	@PostMapping(value="/subtractyears")
//	{
//		"0"
//	}
	public ResponseEntity<String> subtractYearsEndpoint(@RequestBody String years) {
		gameData.subtractYears(Long.valueOf(years));
		return new ResponseEntity<> (gameData.getTimeString(), HttpStatus.OK);
	}
	
	@PostMapping(value="/settime")
//	{
//		"0"
//	}
	public ResponseEntity<String> setTimeEndpoint(@RequestBody String seconds) {
		gameData.setTime(Long.valueOf(seconds));
		return new ResponseEntity<> (gameData.getTimeString(), HttpStatus.OK);
	}
	
	// saves:
	
	@PostMapping(value = "/savecharacter") 
//	{
//	  "john"
//  }
	public ResponseEntity<String> saveCharacterEndpoint(@RequestBody String charName) {
		
		if (saveCharacterToFile(gameData.getCharacterInAnyMap(charName))) {
			return new ResponseEntity<> (charName + " saved", HttpStatus.OK);
		} else {
			return new ResponseEntity<> (charName + " saving error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/getcharactersaves/{sortByDateCreated}")
	public ResponseEntity<String> loadSavedCharacterNamesEndpoint(@PathVariable boolean sortByDateCreated) {
		
		try {
			return new ResponseEntity<> (new ObjectMapper().writeValueAsString(loadCharacterNames(sortByDateCreated)), HttpStatus.OK);
		} catch (JsonProcessingException e) {
			return new ResponseEntity<> ("Error getting game saves", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "/loadcharacter") // loading adds character not in map
//	{
//	  "clarence"
//  }
	public ResponseEntity<String> loadCharacterEndpoint(@RequestBody String charName) {
		
		if (loadCharacterFromFile(charName)) {
			return new ResponseEntity<> (charName + " loaded (added not in the map)", HttpStatus.OK);
		} else {
			return new ResponseEntity<> (charName + " loading error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "/deletecharacter") 
//	{
//	  "ben"
//  }
	public ResponseEntity<String> deleteCharacterEndpoint(@RequestBody String charName) {
		
		if (deleteCharacterFile(charName)) {
			return new ResponseEntity<> (charName + " deleted", HttpStatus.OK);
		} else {
			return new ResponseEntity<> (charName + " deletion error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// groups:
	
	@PostMapping(value = "/savegroup") 
//	{
//	  "groupName":"groupName",
//	  "character1": "john",
//	  "character2": "jimp"
//  }
	public ResponseEntity<String> saveGroupEndpoint(@RequestBody String groupBody) {
		Vector<String> charNames = new Vector<String>();
		String groupName = "unnamedGroup";
        
		JSONObject groupJson = new JSONObject(groupBody);
		Iterator<String> keys = groupJson.keys();
		
		while(keys.hasNext()) {
		    String key = keys.next();
//		    System.out.println("key: " + key);
		    if (key.equals("groupName")) {
		    	groupName = (String) groupJson.get(key);
		    } else {
		    	String charName = (String) groupJson.get(key);
//		    	System.out.println("character: " + charName);
		    	charNames.add(charName);
		    }
		}
        
		if (saveGroupToFile(groupName, charNames)) {
			return new ResponseEntity<> (groupName + " saved", HttpStatus.OK);
		} else {
			return new ResponseEntity<> (groupName + " saving error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/getgroupsaves")
	public ResponseEntity<String> loadSavedGroupNamesEndpoint() {
		
		try {
			return new ResponseEntity<> (new ObjectMapper().writeValueAsString(loadGroupNames()), HttpStatus.OK);
		} catch (JsonProcessingException e) {
			return new ResponseEntity<> ("Error getting game saves", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "/loadgroup") // loading adds character not in map
//	{
//	  "clarences"
//  }
	public ResponseEntity<String> loadGroupEndpoint(@RequestBody String groupName) {
		
		if (loadGroupFromFile(groupName)) {
			return new ResponseEntity<> (groupName + " loaded (added not in the map)", HttpStatus.OK);
		} else {
			return new ResponseEntity<> (groupName + " loading error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "/deletegroup") 
//	{
//	  "bens"
//  }
	public ResponseEntity<String> deleteGroupEndpoint(@RequestBody String groupName) {
		
		if (deleteGroupFile(groupName)) {
			return new ResponseEntity<> (groupName + " deleted", HttpStatus.OK);
		} else {
			return new ResponseEntity<> (groupName + " deletion error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// states:
	
	@PostMapping(value = "/savegame") 
//	{
//	  "game1"
//  }
	public ResponseEntity<String> saveGameStateEndpoint(@RequestBody String gameName) {
		
		if (saveGameState(gameName)) {
			return new ResponseEntity<> (gameName + " saved", HttpStatus.OK);
		} else {
			return new ResponseEntity<> (gameName + " saving error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/getgamesaves") 
	public ResponseEntity<String> loadSavedGameNamesEndpoint() {
		
		try {
			return new ResponseEntity<> (new ObjectMapper().writeValueAsString(loadSavedGameNames()), HttpStatus.OK);
		} catch (JsonProcessingException e) {
			return new ResponseEntity<> ("Error getting game saves", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "/loadgame") 
//	{
//	  "game1"
//  }
	public ResponseEntity<String> loadGameStateEndpoint(@RequestBody String gameName) {
		
		if (loadGameState(gameName)) {
			return new ResponseEntity<> (gameName + " loaded", HttpStatus.OK);
		} else {
			return new ResponseEntity<> (gameName + " loading error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "/deletegame") 
//	{
//	  "game1"
//  }
	public ResponseEntity<String> deleteGameStateEndpoint(@RequestBody String gameName) {
		
		if (deleteGameState(gameName)) {
			return new ResponseEntity<> (gameName + " deleted", HttpStatus.OK);
		} else {
			return new ResponseEntity<> (gameName + " deletion error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	// ----- ----- ^ API STUFF ----- ----- v GameData stuff ----- -----
	
	
	public Vector<DndCharacter> getCharacters(boolean inMap) {
		if (!inMap) {
			return gameData.getCharactersNotInMap();
		} else {
			return gameData.getCharactersInMap();
		}
	}
	
	public Vector<String> getCharacterNames(boolean inMap) {
		Vector<String> result = new Vector<>();
		
		if (getCharacters(inMap) != null) {
			for (DndCharacter d: getCharacters(inMap)) {
				result.add(d.getCharName());
			}
		}
		
		return result;
	}
	
	public Vector<String> getAllCharacterNames() {
		Vector<String> result = getCharacterNames(true);
		result.addAll(getCharacterNames(false));
		return result;
	}
	
	public void addCharacterToGame(DndCharacter c, String mapLocation) { // mapLocation = "" for not in map
		if(StringUtils.hasText(mapLocation)) {
			gameData.addCharacterInMap(c, mapLocation);
		} else {
			gameData.addCharacterNotInMap(c);
		}
		
	}
	
	public Vector<String> getPCs() {
		Vector<String> result = getPCsInMap();

		Vector<DndCharacter> nonMapChars = getCharacters(false);
		if (nonMapChars != null) {
			for (DndCharacter d: nonMapChars) { // not in map
				if (!d.isNpc()) {
					result.add(d.getCharName());
				}
			}
		}
		
		return result;
	}
	
	public Vector<String> getPCsInMap() {
		Vector<String> result2 = new Vector<>();
		
		Vector<DndCharacter> inMapChars = getCharacters(true);
		if (inMapChars != null) {
			for (DndCharacter d: inMapChars) { // in map
				if (!d.isNpc()) {
					result2.add(d.getCharName());
				}
			}
		}
		
		return result2;
	}
	
	// Save to text file:
	
	public Vector<String> loadCharacterNames(boolean sortByDateSaved) {
		Vector<String> charList = new Vector<>(); // sorted alphabetically?
		Map<Long, String> tempCharlist = new TreeMap<>(); // for sorting by date
		
        File f = new File("gameSaves/Characters");
        File[] listOfFiles = f.listFiles();
        
        Long noDateLong = (long) 0;
        
        if (listOfFiles != null) {
	        for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					String charFileName = listOfFiles[i].getName();
//				    System.out.println("File " + charFileName);
					String charWithoutTxt = charFileName.substring(0, charFileName.length()-4);
					
					String characterName;	
					Long dateLong;
					try {
						// attempt to split char & date
						dateLong = Long.parseLong(charWithoutTxt.substring(charWithoutTxt.length()-13));
						characterName = charWithoutTxt.substring(0, charWithoutTxt.length()-13);
					} catch (StringIndexOutOfBoundsException ex) {
//						System.out.println("StringIndexOutOfBoundsException on: " + charWithoutTxt);
						dateLong = noDateLong++;
						characterName = charWithoutTxt;
					} catch (NumberFormatException e) {
//						System.out.println("NumberFormatException on: " + charWithoutTxt.substring(charWithoutTxt.length()-11) + ": " + e.getMessage());
						dateLong = noDateLong++;
						characterName = charWithoutTxt;
					}
					
					if (sortByDateSaved) {
						tempCharlist.put(dateLong, characterName);
					} else { // sort alphabetically (already so)
						charList.add(characterName);
					}
//					System.out.println("characterName = " + characterName + ", dateLong = " + dateLong);

				} else if (listOfFiles[i].isDirectory()) {
//				    System.out.println("Directory " + listOfFiles[i].getName());
				}
			}
	        
	        if (sortByDateSaved) {
	        	ArrayList<Long> keys = new ArrayList<Long>(tempCharlist.keySet());
	        	for (int i = keys.size()-1; i>=0; i--) {
	    			Long key = keys.get(i);
//	    			System.out.println("sorted name = " + tempCharlist.get(key));
	    			charList.add(tempCharlist.get(key)); // add in chronological order
				}
	    	}
        }

		return charList;
	}
	
	private String getFileNameFromCharName(String charName) {
		File f = new File("gameSaves/Characters");
        File[] listOfFiles = f.listFiles();
        
        if (listOfFiles != null) {
	        for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					String charFileName = listOfFiles[i].getName();
//					System.out.println("File " + charFileName);
					if (charFileName.contains(charName)) {
						return charFileName;
					}	
				}
			}
        }
		
		return null;
	}

	public boolean loadCharacterFromFile(String charName) {
		try {	
			String charFileName = getFileNameFromCharName(charName);
			if (charFileName == null) {
				return false;
			}
			String filename = "gameSaves/Characters/" + charFileName;
			FileInputStream file = new FileInputStream(filename);
	        ObjectInputStream in = new ObjectInputStream(file);
	          
	        // Method for deserialization of object
	        DndCharacter loadedCharacter = (DndCharacter) in.readObject();
	          
	        in.close();
	        file.close();
	        
	        if (getAllCharacterNames() != null) {
				if (getAllCharacterNames().contains(loadedCharacter.getCharName())) {
					System.out.println("Character " + loadedCharacter.getCharName() + " already exists. Cannot load from file :(");
					return false;
				}
			}
	        
	        gameData.addCharacterNotInMap(loadedCharacter);
	        System.out.println("Loaded character: " + charName);
	        return true;
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;

	}
	
	public boolean saveCharacterToFile(DndCharacter saveCharacter) {
		
		try {
			deleteCharacterFile(saveCharacter.getCharName());
			
			String timeStr = Long.toString(new Date().getTime()); // get time & add last 8 digits as timestamp
//			System.out.println("timeStr = " + timeStr + ", timeStr length = " + timeStr.length());
			String filename = saveCharacter.getCharName() + timeStr + ".txt";
			String filepath = "gameSaves/Characters/" + filename;
			FileOutputStream fileOut = new FileOutputStream(filepath);
	        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(saveCharacter);
		
	        objectOut.close();
	        System.out.println("character succesfully saved to file: " + filename);
	        return true;
        
        } catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
		
		
	}
	
	public boolean deleteCharacterFile(String charName) {
		
		String charFileName = getFileNameFromCharName(charName);
		if (charFileName == null) {
			return false;
		}
		
		File myObj = new File("gameSaves/Characters/" + charFileName); 
	    if (myObj.delete()) { 
	      System.out.println("Deleted the file: " + myObj.getName());
	    } else {
	      System.out.println("Failed to delete the file.");
	      return false;
	    } 
		
		return true; // return true if success
	}
	
	// groups:
	
	public Vector<String> loadGroupNames() {  // GROUPS are just lists of character names!
		Vector<String> charList = new Vector<>();
		
        File f = new File("gameSaves/Groups");
        File[] listOfFiles = f.listFiles();
        
        if (listOfFiles != null) {
	        for (int i = 0; i < listOfFiles.length; i++) {
	        	  if (listOfFiles[i].isFile()) {
	        		  String groupFileName = listOfFiles[i].getName();
//	        	    System.out.println("File " + charFileName);
	        	    charList.add(groupFileName.substring(0, groupFileName.length()-4));
	        	  } else if (listOfFiles[i].isDirectory()) {
//	        	    System.out.println("Directory " + listOfFiles[i].getName());
	        	  }
	        }
        }
		
		return charList;
	}

	public boolean loadGroupFromFile(String groupName) {
		try {	
			String filename = "gameSaves/Groups/" + groupName + ".txt";
			FileInputStream file = new FileInputStream(filename);
	        ObjectInputStream in = new ObjectInputStream(file);
	          
	        // Method for deserialization of object
	        Vector<String> loadedCharacters = (Vector<String>) in.readObject();
	          
	        in.close();
	        file.close();
	        
	        for (String charName: loadedCharacters) {
	        	loadCharacterFromFile(charName);
	        }
	        
	        System.out.println("Loaded group: " + groupName);
	        return true;
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;

	}
	
	public boolean saveGroupToFile(String groupName, Vector<String> saveGroup) {

		try {	
			
			for (String name: saveGroup) {
				saveCharacterToFile(gameData.getCharacterInAnyMap(name));
			}
			
//			int timeAdded = (int) (new Date().getTime()/1000);
			String filename = groupName + ".txt";
			String filepath = "gameSaves/Groups/" + filename;
			FileOutputStream fileOut = new FileOutputStream(filepath);
	        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(saveGroup);
		
	        objectOut.close();
	        System.out.println("Group succesfully saved to file: " + filename);
	        return true;
        
        } catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
		
		
	}
	
	public boolean deleteGroupFile(String charName) {
		
		File myObj = new File("gameSaves/Groups/" + charName + ".txt"); 
	    if (myObj.delete()) { 
	      System.out.println("Deleted the group file: " + myObj.getName());
	    } else {
	      System.out.println("Failed to delete the group file.");
	      return false;
	    } 
		
		return true; // return true if success
	}
	
	// states:
	
	public Vector<String> loadSavedGameNames() {
		Vector<String> gamesList = new Vector<>();
		
        File f = new File("gameSaves/GameStates");
        File[] listOfFiles = f.listFiles();
        
        if (listOfFiles != null) {
	        for (int i = 0; i < listOfFiles.length; i++) {
	        	  if (listOfFiles[i].isFile()) {
	        		  String gameFileName = listOfFiles[i].getName();
//	        	    System.out.println("File " + gameFileName);
	        	    gamesList.add(gameFileName.substring(0, gameFileName.length()-4));
	        	  } else if (listOfFiles[i].isDirectory()) {
//	        	    System.out.println("Directory " + listOfFiles[i].getName());
	        	  }
	        }
        }
		
		return gamesList;
	}

	public boolean loadGameState(String gameName) {
		try {	
			String filename = "gameSaves/GameStates/" + gameName + ".txt";
			FileInputStream file = new FileInputStream(filename);
	        ObjectInputStream in = new ObjectInputStream(file);
	          
	        // Method for deserialization of object
	        GameData loadedGame = (GameData) in.readObject();
	          
	        in.close();
	        file.close();
	        
	        this.gameData = loadedGame;
	        System.out.println("Loaded game " + gameName);
	        return true;
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;

	}
	
	public boolean saveGameState(String saveAsName) {
		
		try {	
//			int timeAdded = (int) (new Date().getTime()/1000);
			String filename = saveAsName + ".txt";
			String filepath = "gameSaves/GameStates/" + filename;
			FileOutputStream fileOut = new FileOutputStream(filepath);
	        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(gameData);
		
	        objectOut.close();
	        System.out.println("game state succesfully saved to file: " + filename);
	        return true;
        
        } catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
		
		
	}
	
	public boolean deleteGameState(String gameName) {
		
		File myObj = new File("gameSaves/GameStates/" + gameName + ".txt"); 
	    if (myObj.delete()) { 
	      System.out.println("Deleted the file: " + myObj.getName());
	    } else {
	      System.out.println("Failed to delete the file.");
	      return false;
	    } 
		
		return true; // return true if success
	}
	
	// dm time controls?
	
//	public void addAllies (DndCharacter c, Vector<String> allies) {
//		
//	}
//	
//	public void removeAllies (DndCharacter c, Vector<String> allies) {
//		
//	}
	
}
