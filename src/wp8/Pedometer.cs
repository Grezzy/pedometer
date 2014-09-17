using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WPCordovaClassLib.Cordova;
using WPCordovaClassLib.Cordova.Commands;
using WPCordovaClassLib.Cordova.JSON;


namespace Cordova.Extension.Commands
{
    public class Pedometer : BaseCommand
    {

        public void init(string options)
        {
            try
            {
                DispatchCommandResult(new PluginResult(PluginResult.Status.OK, ""));
            }
            catch (Exception ex)
            {
                DispatchCommandResult(new PluginResult(PluginResult.Status.ERROR, new { error = "close", message = ex.Message }));
            }
        }

        public void start(string options)
        {
            try
            {
                DispatchCommandResult(new PluginResult(PluginResult.Status.OK, ""));
            }
            catch (Exception ex)
            {
                DispatchCommandResult(new PluginResult(PluginResult.Status.ERROR, new { error = "close", message = ex.Message }));
            }
        }

        public void stop(string options)
        {
            try
            {
                DispatchCommandResult(new PluginResult(PluginResult.Status.OK, ""));
            }
            catch (Exception ex)
            {
                DispatchCommandResult(new PluginResult(PluginResult.Status.ERROR, new { error = "close", message = ex.Message }));
            }
        }
    }   
        
}
