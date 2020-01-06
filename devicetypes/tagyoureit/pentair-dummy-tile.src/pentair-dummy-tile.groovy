/**
 *  Copyright 2018 Russ Goldin
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
metadata {
	definition (name: "Pentair Dummy Tile", namespace: "tagyoureit", author: "Russ Goldin") {
    	capability "Switch"
	}

	tiles {
		standardTile("dummy", "dummy", width:1, height:1, inactiveLabel: false, decoration: "flat") {
        	state("dummy", label:'') 
		}
                     		
		main "dummy"
		details(["dummy"])
	}
}

def installed() {

}

def updated() {
   
}

def initialize() {

}

def parse(String description) {	

}