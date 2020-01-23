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

metadata {
definition (name: "Pentair Pool Control Switch", namespace: "tagyoureit", author: "Russ Goldin") {
    capability "Switch"
    command onConfirmed
    command offConfirmed
    // attribute "friendlyName", "string"
    attribute "id", "number"
    attribute "name", "string"
    attribute "freeze", "boolean"
    attribute "showInFeatures", "boolean"
    attribute "isActive", "boolean"
    attribute "eggTimer", "number"
    attribute "type", "JSON_OBJECT"
    attribute "isOn", "boolean"
    attribute "equipmentType", "string"

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
tiles {
    multiAttributeTile(name:"switch", type: "generic", width: 1, height: 1, canChangeIcon: true)  {
        tileAttribute("device.switch", key: "PRIMARY_CONTROL") {
            attributeState "off", label: '${name}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff", nextState: "turningOn"           
            attributeState "on", label: '${name}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#79b821", nextState: "tuningOff"
            attributeState "turningOn", label:'${name}', icon:"st.switches.switch.on", backgroundColor:"#00a0dc", nextState: "on"
            attributeState "turningOff", label:'${name}', icon:"st.switches.switch.off", backgroundColor:"#ffffff", nextState: "off"
        }
        // Note - this Approach works to display this name in the Child Device but does not carry through to the parent. Multi-attribute tiles do not work on a childTile??
        tileAttribute ("device.typeDesc", key: "SECONDARY_CONTROL") {
            attributeState "typeDesc", label:'Type: ${currentValue}'
        }		
    }

    }
main "switch"
details "switch", "typeLabel"
}

def installed() {	
manageData()
}

def manageData() {
// log.debug "device ${device} data... ${getDataValue('freeze')}"
// def cF = getDataValue("data")?.type
// if (cf) sendEvent(name: "type", value: cf, isStateChange: true, displayed: false)
// def freeze = getDataValue("data")?.freeze
// if (freeze) sendEvent(name: "freeze", value: freeze, isStateChange: true, displayed: false)

}

def parse(String description) {
try {
    log.debug "parse in control switch ${description}"
        def pair = description.split(":")
        createEvent(name: pair[0].trim(), value: pair[1].trim())
    }
    catch (java.lang.ArrayIndexOutOfBoundsException e) {
        log.debug "Error! " + e   
}

}
def evaluate(){
        state.isOn ? onConfirmed() : offConfirmed()
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

    log.debug "child ${this} state: ${state}"
        log.debug "turning on id:  ${state.id}"
    parent.childOn(state.id)
    sendEvent(name: "switch", value: "turningOn", displayed:false,isStateChange:false)    
}

def off() {
log.debug "child"
    log.info "child ${device.displayName} state: ${state}"
    log.debug "child ${device.displayName} state: ${state}"
    parent.childOff(state.id)
    sendEvent(name: "switch", value: "turningOff", displayed:false,isStateChange:false)
}

def setType(name) {
//log.debug("Set type to ${name}")
sendEvent(name: "type", value: name, displayed:false)
}


def updateState(data){
log.debug "${device.displayName} received ${data}"
    data.each {key, val->
        log.debug "key ${key} and val ${val} and is some type of map? ${val instanceof groovy.json.internal.LazyMap ||val instanceof Map}"
        switch (key){
                case "type":
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
        sendEvent(name: "${key}Desc", value: val.desc)
	} else { log.debug("no change: ${key}: (${val})") }
}