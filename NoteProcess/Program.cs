using System;
using System.Collections.Generic;
using Melanchall.DryWetMidi.Smf;
using Melanchall.DryWetMidi.Smf.Interaction;

namespace NoteProcess
{
    public class NoteInfo
        {
         public MetricTimeSpan Time { get; set; }
         public MetricTimeSpan Length { get; set; }
         public Melanchall.DryWetMidi.MusicTheory.NoteName Note { get; set; }
         public int Octave {get; set; }
        }
    
    public class Program
    {
        static void Main(string[] args) {
            var midiFile = MidiFile.Read("Alphabet_Song.mid");
            TempoMap tempoMap = midiFile.GetTempoMap();
            List<NoteInfo> GetNotesInfo = new List<NoteInfo>();
            foreach(var item in midiFile.GetNotes())
            {
                MetricTimeSpan metricTime = item.TimeAs<MetricTimeSpan>(tempoMap);
                MetricTimeSpan metricLength = item.LengthAs<MetricTimeSpan>(tempoMap);
                GetNotesInfo.Add(new NoteInfo() {
                        Time = metricTime,
                        Length = metricLength,
                        Note = item.NoteName,
                        Octave = item.Octave
                    }
                );
                Console.WriteLine(item.Octave);
            }
        }
    }
}
