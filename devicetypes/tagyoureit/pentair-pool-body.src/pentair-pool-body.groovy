/**
 *  Copyright 2015 SmartThings
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
import groovy.json.JsonSlurper
import groovy.json.JsonParserType
metadata {
	definition (name: "Pentair Pool Body", namespace: "tagyoureit", author: "Russ Goldin") {
		capability "Switch"
        capability "Temperature Measurement"
        command onConfirmed
        command offConfirmed
        command "tempUp"
        command "tempDown"
        command "setLevel", ["number"] 
        attribute "isActive", "boolean"
        attribute "heatMode", "JSON_OBJECT"
        attribute "heatStatus", "JSON_OBJECT"
        attribute "circuit", "number"
        attribute "isOn", "boolean"
        attribute "name", "string"
        attribute "id", "number"
        attribute "setPoint", "number"
	}

	// simulator metadata
	simulator {
		// status messages
		status "on": "switch:on"
		status "off": "switch:off"

		// reply messages
		reply "on": "switch:on"
		reply "off": "switch:off"
	}

	// UI tile definitions
	tiles (scale: 2) {
		multiAttributeTile(name:"switch", type: "generic", width: 2, height: 2, canChangeIcon: true)  {
        	tileAttribute("device.switch", key: "PRIMARY_CONTROL") {
                attributeState "off", label: '${name}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff", nextState: "turningOn"           
                attributeState "on", label: '${name}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#79b821", nextState: "tuningOff"
                attributeState "turningOn", label:'${name}', icon:"st.switches.switch.on", backgroundColor:"#00a0dc", nextState: "on"
                attributeState "turningOff", label:'${name}', icon:"st.switches.switch.off", backgroundColor:"#ffffff", nextState: "off"
            }
            // Note - this Approach works to display this name in the Child Device but does not carry through to the parent. Multi-attribute tiles do not work on a childTile??
            // tileAttribute ("device.friendlyName", key: "SECONDARY_CONTROL") {
        	// 	attributeState "name", label:'${currentValue}'
    		// }
            }		
        
  
        

        // see ref: https://github.com/SmartThingsCommunity/SmartThingsPublic/blob/master/devicetypes/smartthings/tile-ux/tile-multiattribute-thermostat.src/tile-multiattribute-thermostat.groovy
        multiAttributeTile(name:"thermostatFull", type:"thermostat", width:6, height:4) {
                tileAttribute("device.temperature", key: "PRIMARY_CONTROL") {
                    attributeState("temp", label:'${currentValue}', unit:"dF")
                }
                tileAttribute("device.temperature", key: "VALUE_CONTROL") {
                    attributeState("VALUE_UP", action: "tempUp")
                    attributeState("VALUE_DOWN", action: "tempDown")
                }
                tileAttribute("device.thermostatOperatingState", key: "OPERATING_STATE") {
                    attributeState("off", backgroundColor:"#00A0DC", label: '${name} something')
                    attributeState("heater", backgroundColor:"#e86d13")
                    attributeState("solar", backgroundColor:"#e86d13")
                    attributeState("cooling", backgroundColor:"#00A0DC")

                    attributeState("idle", backgroundColor:"#00A0DC")
                    attributeState("heating", backgroundColor:"#e86d13")
                }
                tileAttribute("device.thermostatMode", key: "THERMOSTAT_MODE") {
                    attributeState("off", label:'${name}')
                    attributeState("heater", label:'${name}')
                    attributeState("solar", label:'${name}')
                    attributeState("solarpref", label:'${name}')
                    attributeState("heatpump", label:'${name}')
                    attributeState("heatpumppref", label:'${name}')

                }
                tileAttribute("device.heatingSetpoint", key: "HEATING_SETPOINT") {
                    attributeState("heatingSetpoint", label:'${currentValue} set point', unit:"dF", defaultState: true)
                }
            }
            
            valueTile("temperatureCurr", "device.temperatureCurr", width: 2, height: 2) {
            state("temperature", label:'Water Temp:${currentValue}', unit: "dF",
                backgroundColors:[
                    [value: 0, color: "#153591"],
                    [value: 7, color: "#1e9cbb"],
                    [value: 15, color: "#90d2a7"],
                    [value: 23, color: "#44b621"],
                    [value: 28, color: "#f1d801"],
                    [value: 35, color: "#d04e00"],
                    [value: 37, color: "#bc2323"],
                    // Fahrenheit
                    [value: 40, color: "#153591"],
                    [value: 44, color: "#1e9cbb"],
                    [value: 59, color: "#90d2a7"],
                    [value: 74, color: "#44b621"],
                    [value: 84, color: "#f1d801"],
                    [value: 95, color: "#d04e00"],
                    [value: 96, color: "#bc2323"]
                ]
            )
        }
        valueTile("temperatureSP", "device.heatingSetpoint", width: 2, height: 2) {
            state("temperature", label:'Set Point: ${currentValue}', unit: "dF",
                backgroundColors:[
                    [value: 0, color: "#153591"],
                    [value: 7, color: "#1e9cbb"],
                    [value: 15, color: "#90d2a7"],
                    [value: 23, color: "#44b621"],
                    [value: 28, color: "#f1d801"],
                    [value: 35, color: "#d04e00"],
                    [value: 37, color: "#bc2323"],
                    // Fahrenheit
                    [value: 40, color: "#153591"],
                    [value: 44, color: "#1e9cbb"],
                    [value: 59, color: "#90d2a7"],
                    [value: 74, color: "#44b621"],
                    [value: 84, color: "#f1d801"],
                    [value: 95, color: "#d04e00"],
                    [value: 96, color: "#bc2323"]
                ]
            )
        }
        
        multiAttributeTile(name:"thermostatFull", type:"thermostat", width:6, height:4) {
    tileAttribute("device.temperature", key: "PRIMARY_CONTROL") {
        attributeState("temp", label:'${currentValue}', unit:"dF", defaultState: true)
    }
    tileAttribute("device.temperature", key: "VALUE_CONTROL") {
        attributeState("VALUE_UP", action: "tempUp")
        attributeState("VALUE_DOWN", action: "tempDown")
    }
    tileAttribute("device.humidity", key: "SECONDARY_CONTROL") {
        attributeState("humidity", label:'${currentValue}%', unit:"%", defaultState: true)
    }
    tileAttribute("device.thermostatOperatingState", key: "OPERATING_STATE") {
        attributeState("idle", backgroundColor:"#00A0DC")
        attributeState("heating", backgroundColor:"#e86d13")
        attributeState("cooling", backgroundColor:"#00A0DC")
    }
    tileAttribute("device.thermostatMode", key: "THERMOSTAT_MODE") {
        attributeState("off", label:'${name}')
        attributeState("heat", label:'${name}')
        attributeState("cool", label:'${name}')
        attributeState("auto", label:'${name}')
    }
    tileAttribute("device.heatingSetpoint", key: "HEATING_SETPOINT") {
        attributeState("heatingSetpoint", label:'${currentValue}', unit:"dF", defaultState: true)
    }
    tileAttribute("device.coolingSetpoint", key: "COOLING_SETPOINT") {
        attributeState("coolingSetpoint", label:'${currentValue}', unit:"dF", defaultState: true)
    }
}
        valueTile("tOpStText", "device.thermostatOperatingState", decoration: "flat", width: 2, height: 2) {
            state "thermostatOperatingState", label:'Current heat status: ${currentValue}'
        }
    
        valueTile("tModeText", "device.thermostatMode", decoration: "flat", width: 2, height: 2) {
                state "thermostatMode", label:'Current heat Mode: ${currentValue}'
            }        
        }

    controlTile("levelSliderControl", "device.level", "slider", height: 2,
             width: 2, inactiveLabel: false, range: getRange()) {
    state "level", action:"setLevel", backgroundColor:"#FF3300"
    } 
	main "switch"
	details "switch", "thermostatFull", "temperature", "tOpStText", "tModeText", "temperatureCurr", "temperatureSP", "levelSliderControl"
}

def installed() {	
    log.debug "installed ${device}"
}

def parse(String description) {
	try {
         def pair = description.split(":")
         createEvent(name: pair[0].trim(), value: pair[1].trim())
     }
     catch (java.lang.ArrayIndexOutOfBoundsException e) {
           log.debug "Error! " + e   
    }
	
}

def onConfirmed() {
    //log.debug("CONF ${device} turned on")
	sendEvent(name: "switch", value: "on", displayed:true)    
}

def offConfirmed() {
	//log.debug("CONF ${device} turned off")
	sendEvent(name: "switch", value: "off", displayed:true)  
}

def on() {
    log.debug "turning on circuit:  ${state.circuit}"
	parent.childOn(state.circuit)
    sendEvent(name: "switch", value: "turningOn", displayed:false,isStateChange:false)    
}

def off() {
    log.debug "turning off circuit:  ${state.circuit}"
	parent.childOff(state.circuit)
    sendEvent(name: "switch", value: "turningOff", displayed:false,isStateChange:false)
}

def setFriendlyName(name) {
   //log.debug("Set FName to ${name}")
   sendEvent(name: "friendlyName", value: name, displayed:false)
}

// def setCircuitFunction(name) {
//    //log.debug("Set CircuitFunction to ${name}")
//    sendEvent(name: "circuitFunction", value: name, displayed:false)
// }

def evaluate(){

        state.isOn ? onConfirmed() : offConfirmed()
        sendEvent(name: "temperature", value: state.temp, displayed:true) 
        sendEvent(name: "level", value: state.setPoint, displayed:false)
        sendEvent(name: "heatingSetpoint", value: state.setPoint, displayed: true) 
        sendEvent(name: "thermostatOperatingState", value: state.heatStatus.name, displayed: true)
        sendEvent(name: "thermostatMode", value: state.heatMode.name, displayed: true)

        sendEvent(name: "temperatureCurr", value: state.temp, displayed:true) 
        sendEvent(name: "temperatureSP", value: state.temp, displayed: true)  

    //  if (currentState("heatStatus")) sendEvent(name: "heater", value: currentState("heatStatus")?.name=="off"?"off":"on", displayed: true)

}

def updateScale(scale){
     valEvent("scale", scale)
     state?.scale = scale.name == "F" ? "dF" : "dC"
}



def updateState(data){
    
    log.debug "${device.displayName} received ${data}"
     data.each {key, val->
           
            
            log.debug "key ${key} and val ${val} and is some type of map? ${val instanceof groovy.json.internal.LazyMap ||val instanceof Map}"
         switch (key){
                    case "heatMode":
                    case "heatStatus":
                        if (val instanceof groovy.json.internal.LazyMap ||val instanceof Map) valEvent(key, val)
                        break
                    default:
                        log.debug "key ${key} and val ${val}"
                        genericEvent(key, val)
                        break
            } 

     }
    log.debug "${device.displayName} state=${state}"
    evaluate()
}

def genericEvent(key, val){
	state[key] = val
    log.debug "genericEvent key:val ${key}:${val}  origVal:${device.currentState(key)?.value}  isStatechange:${isStateChange(device, key, val.toString())}"
    if(isStateChange(device, key, val.toString()) ){
		log.debug("UPDATED | ${key} : (${val}) | Original State: (${val})")
		sendEvent(name: key, value: val, displayed: true)
	} else { log.debug("no change: ${key}: (${val})") }
}

// for any data with val,desc,name
def valEvent(key, valstr) {
    def val
    if (valstr instanceof String) {val = parent.convertLazyMapToLinkedHashMap(valstr) }
    else {val = valstr}

    log.debug "does ${key} have val.val? -- ${val?.val}"

    def origVal = device.currentState(key)?.value
    log.debug "val event:  ${key}:${val}   origVal=${origVal}"
	state[key] = val
	if (!origVal && val?.val.toString() != origVal?.val.toString()){
		log.debug("UPDATED || ${key} : (${val}) | Original State: (${origVal})")
		sendEvent(name: key, value: val, displayed: true)
	} else { log.debug("no change: ${key}: (${val})") }
}

def getRange() { 
/*     state?.scale ?: "F"
    sendEvent(name: "scale", value: state?.scale?:"F")
    log.info "state.scale??? ${state.scale}"
    def lowRange = (state?.scale == "F" ? 40 : 104 )
    def highRange = (state?.scale == "F" ? 4 : 40) */
    def lowRange = 40
    def highRange = 104
    return "${lowRange}..${highRange}" }
/*   ACTIONS   */

def tempDown() {
    def ts = state.setPoint
    def adj = state?.scale == "F" ? 1 : 0.5
    def value = ts ? ts - adj : state.scale == "F" ? 80: 30
    state.setPoint = value
    parent.setTempSetPoint(state.id, value)
}
def tempUp() {
    def ts = state.setPoint
    def adj = state?.scale == "F" ? 1 : 0.5
    def value = ts ? ts + adj : state.scale == "F" ? 80: 30
    state.setPoint = value
    parent.setTempSetPoint(state.id, value)
}

def setLevel(value) {
    state.setPoint = value
    parent.setTempSetPoint(state.id, value)
}