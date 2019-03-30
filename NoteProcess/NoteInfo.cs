using System;
using System.Collections.Generic;
using Melanchall.DryWetMidi.Smf;
using Melanchall.DryWetMidi.Smf.Interaction;
using System.Diagnostics;

namespace NoteProcess
{
    public class NoteInfo
    {
        public MetricTimeSpan Time { get; set; }
        public MetricTimeSpan Length { get; set; }
        public Melanchall.DryWetMidi.MusicTheory.NoteName Note { get; set; }
        public int Octave { get; set; }
    }
}
