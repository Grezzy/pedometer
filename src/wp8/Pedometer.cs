using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WPCordovaClassLib.Cordova;
using WPCordovaClassLib.Cordova.Commands;
using WPCordovaClassLib.Cordova.JSON;
using Lumia.Sense;
using System.Runtime.Serialization;


namespace Cordova.Extension.Commands
{
    public class Pedometer : BaseCommand
    {
        private bool isReady;
        private StepCounter stepCounter;
        private bool isRunning;
        private DateTimeOffset? runStarted;
        private PedometerReading latestReading = null;

        public Pedometer()
            : base()
        {
            initialize(null);
        }

        public async void initialize(string options)
        {
            PluginResult result = new PluginResult(PluginResult.Status.OK);
            result.KeepCallback = true;

            try
            {
                if (await StepCounter.IsSupportedAsync())
                {
                    stepCounter = await StepCounter.GetDefaultAsync();
                    isReady = true;
                }

                DispatchCommandResult(result);
            }
            catch (Exception ex)
            {
                result.Message = JsonHelper.Serialize(new { error = "init", message = ex.Message });
                DispatchCommandResult(result);
            }
        }

        public async void isSupported(string options)
        {
            PluginResult result = new PluginResult(PluginResult.Status.OK);
            result.KeepCallback = true;

            try
            {
                bool available = await StepCounter.IsSupportedAsync();
                result.Message = JsonHelper.Serialize(available);
                DispatchCommandResult(result);
            }
            catch (Exception ex)
            {
                result.Message = JsonHelper.Serialize(new { error = "isSupported", message = ex.Message });
                DispatchCommandResult(result);
            }
        }

        public void start(string options)
        {
            try
            {
                isRunning = true;
                runStarted = DateTimeOffset.Now;
                DispatchCommandResult(new PluginResult(PluginResult.Status.OK, null));
            }
            catch (Exception ex)
            {
                DispatchCommandResult(new PluginResult(PluginResult.Status.ERROR, new { error = "start", message = ex.Message }));
            }
        }

        public void stop(string options)
        {
            try
            {
                isRunning = false;
                DispatchCommandResult(new PluginResult(PluginResult.Status.OK, null));
            }
            catch (Exception ex)
            {
                DispatchCommandResult(new PluginResult(PluginResult.Status.ERROR, new { error = "stop", message = ex.Message }));
            }
        }

        public async void getCurrentReading(string options)
        {
            PluginResult result = new PluginResult(PluginResult.Status.OK);
            result.KeepCallback = true;

            try
            {
                PedometerReading reading = new PedometerReading();
                if (!isReady)
                {
                    DispatchCommandResult(new PluginResult(PluginResult.Status.OK, latestReading ?? reading));
                    return;
                }

                StepCounterReading current = await stepCounter.GetCurrentReadingAsync();
                StepCounterReading today = await stepCounter.GetStepCountAtAsync(DateTimeOffset.Now.Date);

                if (null == current && null == today)
                {
                    DispatchCommandResult(new PluginResult(PluginResult.Status.OK, latestReading ?? reading));
                    return;
                }

                if (null == today) today = new StepCounterReading(0, TimeSpan.FromMilliseconds(0), 0, TimeSpan.FromMilliseconds(0), DateTimeOffset.Now.Date);

                reading.todayWalkingSteps = Convert.ToInt32(current.WalkingStepCount - today.WalkingStepCount);
                reading.todayWalkingMinutes = Convert.ToInt32(current.WalkTime.TotalMinutes - today.WalkTime.TotalMinutes);
                reading.todayRunningSteps = Convert.ToInt32(current.RunningStepCount - today.RunningStepCount);
                reading.todayRunningMinutes = Convert.ToInt32(current.RunTime.TotalMinutes - today.RunTime.TotalMinutes);

                if (runStarted.HasValue)
                {
                    StepCounterReading lastRun = await stepCounter.GetStepCountAtAsync(runStarted.Value);
                    reading.lastRunSteps = Convert.ToInt32(current.WalkingStepCount - lastRun.WalkingStepCount);
                    reading.lastRunMinutes = Convert.ToInt32(current.RunTime.TotalMinutes - lastRun.RunTime.TotalMinutes);
                }

                latestReading = reading;

                result.Message = JsonHelper.Serialize(reading);
                DispatchCommandResult(result);
            }
            catch (Exception ex)
            {
                result.Message = JsonHelper.Serialize(new { error = "getCurrentReading", message = ex.Message });
                DispatchCommandResult(result);
            }
        }

    }

    [DataContract]
    public class PedometerReading
    {
        [DataMember]
        public int todayWalkingSteps { get; set; }
        [DataMember]
        public int todayWalkingMinutes { get; set; }
        [DataMember]
        public int todayRunningSteps { get; set; }
        [DataMember]
        public int todayRunningMinutes { get; set; }
        [DataMember]
        public int lastRunSteps { get; set; }
        [DataMember]
        public int lastRunMinutes { get; set; }
    }
}
