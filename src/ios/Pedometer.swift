//
//  Pedometer.swift
//  FireRunner
//
//  Created by Artem Lakomov on 9/21/14.
//
//

import Foundation
import CoreLocation
import CoreMotion

public class Pedometer: CDVPlugin {
    
    var stepCounter = CMPedometer()
    var runStarted = NSDate(timeIntervalSince1970: 0)
    
    
    func initialize(command: CDVInvokedUrlCommand){
        var pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
        commandDelegate.sendPluginResult(pluginResult, callbackId:command.callbackId)
    }
    
    func isSupported(command: CDVInvokedUrlCommand){
        var res = NSDictionary(object: CMPedometer.isStepCountingAvailable(), forKey: "isSupported")        
        var pluginResult = CDVPluginResult(status:CDVCommandStatus_OK, messageAsDictionary:res)
        commandDelegate.sendPluginResult(pluginResult, callbackId:command.callbackId)
    }
    
    func start(command: CDVInvokedUrlCommand){
        self.runStarted = NSDate()
        var pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
        commandDelegate.sendPluginResult(pluginResult, callbackId:command.callbackId)
    }
    
    func stop(command: CDVInvokedUrlCommand){
        self.runStarted = NSDate(timeIntervalSince1970: 0)
        var pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
        commandDelegate.sendPluginResult(pluginResult, callbackId:command.callbackId)
    }
    
    func getCurrentReading(command: CDVInvokedUrlCommand){
        
        self.commandDelegate.runInBackground {
            
            var reading = NSMutableDictionary()
            reading.setObject(0, forKey: "todayWalkingSteps")
            reading.setObject(0, forKey: "todayWalkingMinutes")
            reading.setObject(0, forKey: "todayWalkingMeters")
            reading.setObject(0, forKey: "todayRunningSteps")
            reading.setObject(0, forKey: "todayRunningMinutes")
            reading.setObject(0, forKey: "todayRunningSteps")
            reading.setObject(0, forKey: "lastRunSteps")
            reading.setObject(0, forKey: "lastRunMinutes")
            
            var now = NSDate()
            var gregorian = NSCalendar.currentCalendar()
            var dateComponents = gregorian.components((NSCalendarUnit.CalendarUnitYear | NSCalendarUnit.CalendarUnitMonth | NSCalendarUnit.CalendarUnitDay), fromDate: now)
            var today = gregorian.dateFromComponents(dateComponents)
            
            if(CMPedometer.isStepCountingAvailable()){
                self.stepCounter.queryPedometerDataFromDate(today, toDate: now, withHandler: {
                    data, error in
                    if(data != nil){
                        reading.setObject(data.numberOfSteps, forKey: "todayWalkingSteps")
                    }
                    if(self.runStarted.timeIntervalSince1970 != 0){
                        self.stepCounter.queryPedometerDataFromDate(self.runStarted, toDate: now, withHandler: {
                            data, error in
                            if(data != nil){
                                reading.setObject(data.numberOfSteps, forKey: "lastRunSteps")
                            }
                            var pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAsDictionary  : reading)
                            self.commandDelegate.sendPluginResult(pluginResult, callbackId:command.callbackId)
                        })
                    } else {
                        var pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAsDictionary  : reading)
                        self.commandDelegate.sendPluginResult(pluginResult, callbackId:command.callbackId)
                    }
                })
            } else {
                var pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAsDictionary  : reading)
                self.commandDelegate.sendPluginResult(pluginResult, callbackId:command.callbackId)
            }
        }
        
    }
}

