delete
from user_role;
delete
from app_role;
delete
from app_user;

insert into app_user (id, user_name, encrypted_password, status)
values (nextval('hibernate_sequence'), 'dbuser1', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 'ACTIVE');

insert into app_user (id, user_name, encrypted_password, status)
values (nextval('hibernate_sequence'), 'dbadmin1', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 'ACTIVE');

insert into app_role (id, role_name)
VALUES ((select id from app_user where user_name = 'dbadmin1'), 'ROLE_ADMIN');

insert into app_role (id, role_name)
VALUES ((select id from app_user where user_name = 'dbuser1'), 'ROLE_USER');

insert into user_role (id, user_id, role_id)
VALUES (nextval('hibernate_sequence'),
        (select id from app_user where user_name = 'dbadmin1'),
        (select id from app_role where role_name = 'ROLE_USER'));
insert into user_role (id, user_id, role_id)
VALUES (nextval('hibernate_sequence'),
        (select id from app_user where user_name = 'dbadmin1'),
        (select id from app_role where role_name = 'ROLE_ADMIN'));
insert into user_role (id, user_id, role_id)
values (nextval('hibernate_sequence'),
        (select id from app_user where user_name = 'dbuser1'),
        (select id from app_role where role_name = 'ROLE_USER'));

delete
from spfiles;
delete
from soulpatches;

insert into soulpatches(id, created_at, updated_at, author, description, name, no_servings)
values (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'author 0', 'description 0',
        'clarinetMIDI',
        0),
       (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'author 1', 'description 1',
        'ClassicRingtone',
        0),
       (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'author 2', 'description 2', 'Delay',
        0),
       (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'author 3', 'description 3',
        'DiodeClipper',
        0),
       (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'author 4', 'description 4',
        'MinimumViablePiano',
        0),
       (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'author 5', 'description 5', 'PadSynth',
        0),
       (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'author 6', 'description 6', 'Reverb',
        0),
       (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'author 7', 'description 7', 'SineSynth',
        0),
       (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'author 8', 'description 8', 'SOUL909',
        0),
       (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'author 9', 'description 9', 'ElecBass1',
        0),
       (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'author 10', 'description 10',
        'ElectroPiano',
        0),
       (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'author 11', 'description 11',
        'LatelyBass',
        0);

insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOUL', '/* ------------------------------------------------------------
copyright: "(c)Romain Michon, CCRMA (Stanford University), GRAME"
license: "MIT"
name: "ClarinetMIDI"
Code generated with Faust 2.20.2 (https://faust.grame.fr)
Compilation options: -lang soul-poly -scal -ftz 0
------------------------------------------------------------ */

//----------------------------------------------------------------------------
//  FAUST generated signal processor
//----------------------------------------------------------------------------

processor clarinetMIDI
{
	input event float32 event_freq [[ name: "freq", group: "/v:clarinet/h:midi/freq", min: 50.0f, max: 1000.0f, init: 440.0f, step: 0.00999999978f, meta_style: "knob" ]];
	input event float32 event_bend [[ name: "bend", group: "/v:clarinet/h:midi/bend", min: -2.0f, max: 2.0f, init: 0.0f, step: 0.00999999978f, meta_hidden: "1", meta_midi: "pitchwheel", meta_style: "knob" ]];
	input event float32 event_gain [[ name: "gain", group: "/v:clarinet/h:midi/gain", min: 0.0f, max: 1.0f, init: 0.600000024f, step: 0.00999999978f, meta_style: "knob" ]];
	input event float32 event_envAttack [[ name: "envAttack", group: "/v:clarinet/h:midi/envAttack", min: 0.0f, max: 30.0f, init: 1.0f, step: 0.00999999978f, meta_style: "knob" ]];
	input event float32 event_sustain [[ name: "sustain", group: "/v:clarinet/h:midi/sustain", min: 0.0f, max: 1.0f, init: 0.0f, step: 1.0f, meta_hidden: "1", meta_midi: "ctrl 64", meta_style: "knob" ]];
	input event float32 event_reedStiffness [[ name: "reedStiffness", group: "/v:clarinet/h:otherParams/reedStiffness", min: 0.0f, max: 1.0f, init: 0.5f, step: 0.00999999978f, meta_midi: "ctrl 1", meta_style: "knob" ]];
	input event float32 event_bellOpening [[ name: "bellOpening", group: "/v:clarinet/h:otherParams/bellOpening", min: 0.0f, max: 1.0f, init: 0.5f, step: 0.00999999978f, meta_midi: "ctrl 3", meta_style: "knob" ]];
	input event float32 event_vibratoFreq [[ name: "vibratoFreq", group: "/v:clarinet/h:otherParams/vibratoFreq", min: 1.0f, max: 10.0f, init: 5.0f, step: 0.00999999978f, meta_style: "knob" ]];
	input event float32 event_vibratoGain [[ name: "vibratoGain", group: "/v:clarinet/h:otherParams/vibratoGain", min: 0.0f, max: 1.0f, init: 0.25f, step: 0.00999999978f, meta_style: "knob" ]];
	input event float32 event_outGain [[ name: "outGain", group: "/v:clarinet/h:otherParams/outGain", min: 0.0f, max: 1.0f, init: 0.5f, step: 0.00999999978f, meta_style: "knob" ]];
	input event float32 event_gate [[ name: "gate", group: "/v:clarinet/gate", text: "off|on", boolean ]];
	output stream float32 output0;
	output stream float32 output1;
	float32 fHslider0;
	int32[2] iRec6;
	float32 fHslider1;
	float32[2] fRec12;
	float32 fButton0;
	float32 fHslider2;
	float32[2] fVec0;
	float32 fHslider3;
	int32 fSampleRate;
	float32 fConst0;
	float32 fConst1;
	float32 fHslider4;
	float32[2] fRec15;
	float32 fConst2;
	float32 fConst3;
	float32 fConst4;
	float32 fConst5;
	int32[2] iRec17;
	float32 fConst6;
	float32 fConst7;
	float32 fConst8;
	float32[3] fRec16;
	float32 fHslider5;
	float32 fConst9;
	float32 fHslider6;
	float32[2] fRec19;
	float32[2] fRec13;
	float32 fHslider7;
	int32 IOTA;
	float32[2048] fRec14;
	float32 fConst10;
	float32 fConst11;
	float32 fHslider8;
	float32 fHslider9;
	float32[2] fRec20;
	float32[2] fVec1;
	float32[2] fRec11;
	float32[2048] fRec2;
	float32[2] fRec0;
	bool fUpdated;
	int32[1] iControl;
	float32[11] fControl;
	float32[65536] ftbl0clarinetMIDISIG0;

	// freq [init = 440.0f, min = 50.0f, max = 1000.0f, step = 0.00999999978f]
	event event_freq (float32 val) { fHslider8 = val; fUpdated = true; }
	// bend [init = 0.0f, min = -2.0f, max = 2.0f, step = 0.00999999978f]
	event event_bend (float32 val) { fHslider9 = val; fUpdated = true; }
	// gain [init = 0.600000024f, min = 0.0f, max = 1.0f, step = 0.00999999978f]
	event event_gain (float32 val) { fHslider3 = val; fUpdated = true; }
	// envAttack [init = 1.0f, min = 0.0f, max = 30.0f, step = 0.00999999978f]
	event event_envAttack (float32 val) { fHslider4 = val; fUpdated = true; }
	// sustain [init = 0.0f, min = 0.0f, max = 1.0f, step = 1.0f]
	event event_sustain (float32 val) { fHslider2 = val; fUpdated = true; }
	// reedStiffness [init = 0.5f, min = 0.0f, max = 1.0f, step = 0.00999999978f]
	event event_reedStiffness (float32 val) { fHslider7 = val; fUpdated = true; }
	// bellOpening [init = 0.5f, min = 0.0f, max = 1.0f, step = 0.00999999978f]
	event event_bellOpening (float32 val) { fHslider1 = val; fUpdated = true; }
	// vibratoFreq [init = 5.0f, min = 1.0f, max = 10.0f, step = 0.00999999978f]
	event event_vibratoFreq (float32 val) { fHslider6 = val; fUpdated = true; }
	// vibratoGain [init = 0.25f, min = 0.0f, max = 1.0f, step = 0.00999999978f]
	event event_vibratoGain (float32 val) { fHslider5 = val; fUpdated = true; }
	// outGain [init = 0.5f, min = 0.0f, max = 1.0f, step = 0.00999999978f]
	event event_outGain (float32 val) { fHslider0 = val; fUpdated = true; }
	// gate
	event event_gate (float32 val) { fButton0 = val; fUpdated = true; }

	struct clarinetMIDISIG0
	{
		int32[2] iRec18;
	}

	void instanceInitclarinetMIDISIG0 (clarinetMIDISIG0& this, int sample_rate)
	{
		for (int32 l6 = 0; (l6 < 2); l6 = (l6 + 1)) {
			this.iRec18.at (l6) = 0;
		}
	}

	void fillclarinetMIDISIG0_65536 (clarinetMIDISIG0& this, int count, float32[65536]& table)
	{
		for (int32 i = 0; (i < count); i = (i + 1)) {
			this.iRec18[0] = (this.iRec18[1] + 1);
			table.at (i) = sin ((9.58738019e-05f * float32 ((this.iRec18[0] + -1))));
			this.iRec18[1] = this.iRec18[0];
		}
	}
	clarinetMIDISIG0 newclarinetMIDISIG0() { clarinetMIDISIG0 obj; return obj; }
	void deleteclarinetMIDISIG0 (clarinetMIDISIG0& this) {}
	int getNumInputs() { return 0; }

	int getNumOuputs() { return 2; }

	void classInit (int sample_rate)
	{
		clarinetMIDISIG0 sig0 = newclarinetMIDISIG0();
		instanceInitclarinetMIDISIG0 (sig0, sample_rate);
		fillclarinetMIDISIG0_65536 (sig0, 65536, ftbl0clarinetMIDISIG0);
		deleteclarinetMIDISIG0 (sig0);
	}

	void instanceConstants (int sample_rate)
	{
		fSampleRate = sample_rate;
		fConst0 = min (192000.0f, max (1.0f, float32 (fSampleRate)));
		fConst1 = (1000.0f / fConst0);
		fConst2 = tan ((6283.18555f / fConst0));
		fConst3 = (1.0f / fConst2);
		fConst4 = (((fConst3 + 1.41421354f) / fConst2) + 1.0f);
		fConst5 = (0.0500000007f / fConst4);
		fConst6 = (1.0f / fConst4);
		fConst7 = (((fConst3 + -1.41421354f) / fConst2) + 1.0f);
		fConst8 = (2.0f * (1.0f - (1.0f / pow (fConst2, 2.0f))));
		fConst9 = (1.0f / fConst0);
		fConst10 = (0.00882352982f * fConst0);
		fConst11 = (0.00147058826f * fConst0);
	}

	void instanceResetUserInterface()
	{
		fUpdated = true;
		fHslider0 = 0.5f;
		fHslider1 = 0.5f;
		fButton0 = 0.0f;
		fHslider2 = 0.0f;
		fHslider3 = 0.600000024f;
		fHslider4 = 1.0f;
		fHslider5 = 0.25f;
		fHslider6 = 5.0f;
		fHslider7 = 0.5f;
		fHslider8 = 440.0f;
		fHslider9 = 0.0f;
	}

	void instanceClear()
	{
		for (int32 l0 = 0; (l0 < 2); l0 = (l0 + 1)) {
			iRec6.at (l0) = 0;
		}
		for (int32 l1 = 0; (l1 < 2); l1 = (l1 + 1)) {
			fRec12.at (l1) = 0.0f;
		}
		for (int32 l2 = 0; (l2 < 2); l2 = (l2 + 1)) {
			fVec0.at (l2) = 0.0f;
		}
		for (int32 l3 = 0; (l3 < 2); l3 = (l3 + 1)) {
			fRec15.at (l3) = 0.0f;
		}
		for (int32 l4 = 0; (l4 < 2); l4 = (l4 + 1)) {
			iRec17.at (l4) = 0;
		}
		for (int32 l5 = 0; (l5 < 3); l5 = (l5 + 1)) {
			fRec16.at (l5) = 0.0f;
		}
		for (int32 l7 = 0; (l7 < 2); l7 = (l7 + 1)) {
			fRec19.at (l7) = 0.0f;
		}
		for (int32 l8 = 0; (l8 < 2); l8 = (l8 + 1)) {
			fRec13.at (l8) = 0.0f;
		}
		IOTA = 0;
		for (int32 l9 = 0; (l9 < 2048); l9 = (l9 + 1)) {
			fRec14.at (l9) = 0.0f;
		}
		for (int32 l10 = 0; (l10 < 2); l10 = (l10 + 1)) {
			fRec20.at (l10) = 0.0f;
		}
		for (int32 l11 = 0; (l11 < 2); l11 = (l11 + 1)) {
			fVec1.at (l11) = 0.0f;
		}
		for (int32 l12 = 0; (l12 < 2); l12 = (l12 + 1)) {
			fRec11.at (l12) = 0.0f;
		}
		for (int32 l13 = 0; (l13 < 2048); l13 = (l13 + 1)) {
			fRec2.at (l13) = 0.0f;
		}
		for (int32 l14 = 0; (l14 < 2); l14 = (l14 + 1)) {
			fRec0.at (l14) = 0.0f;
		}
	}

	void init()
	{
		let sample_rate = int(processor.frequency);
		classInit (sample_rate);
		instanceInit (sample_rate);
	}

	void instanceInit (int sample_rate)
	{
		instanceConstants (sample_rate);
		instanceResetUserInterface();
		instanceClear();
	}

	void control()
	{
		fControl[0] = float32 (fHslider0);
		fControl[1] = float32 (fHslider1);
		fControl[2] = (1.0f - fControl[1]);
		fControl[3] = min (1.0f, (float32 (fButton0) + float32 (fHslider2)));
		fControl[4] = exp ((0.0f - (fConst1 / float32 (fHslider4))));
		fControl[5] = ((fControl[3] * float32 (fHslider3)) * (1.0f - fControl[4]));
		fControl[6] = (0.00999999978f * float32 (fHslider5));
		fControl[7] = (fConst9 * float32 (fHslider6));
		fControl[8] = ((0.25999999f * float32 (fHslider7)) + -0.439999998f);
		fControl[9] = (170.0f / float32 (fHslider8));
		iControl[0] = int ((fControl[3] == 0.0f));
		fControl[10] = pow (2.0f, (0.0833333358f * float32 (fHslider9)));
	}

	void run()
	{
		// DSP loop running forever...
		loop
		{
			// Updates control only if needed
			if (fUpdated) { fUpdated = false; control(); }

			// Computes one sample
			iRec6[0] = 0;
			fRec12[0] = ((fControl[1] * fRec12[1]) + (fControl[2] * fRec11[1]));
			float32 fRec10 = (fRec12[0] + float32 (iRec6[1]));
			fVec0[0] = fControl[3];
			fRec15[0] = (fControl[5] + (fControl[4] * fRec15[1]));
			iRec17[0] = ((1103515245 * iRec17[1]) + 12345);
			fRec16[0] = ((4.65661287e-10f * float32 (iRec17[0])) - (fConst6 * ((fConst7 * fRec16[2]) + (fConst8 * fRec16[1]))));
			float32 fTemp0 = (fRec15[0] * ((fConst5 * (fRec16[2] + (fRec16[0] + (2.0f * fRec16[1])))) + 1.0f));
			fRec19[0] = (fControl[7] + (fRec19[1] - floor ((fControl[7] + fRec19[1]))));
			float32 fTemp1 = ftbl0clarinetMIDISIG0.at (int32 ((65536.0f * fRec19[0])));
			float32 fTemp2 = (fControl[6] * fTemp1);
			fRec13[0] = (fTemp0 + (fRec0[1] + fTemp2));
			float32 fTemp3 = (0.0f - fRec13[1]);
			fRec14.at ((int (IOTA) & int (2047))) = ((fTemp2 + fTemp0) + (fTemp3 * max (-1.0f, min (1.0f, ((fControl[8] * fTemp3) + 0.699999988f)))));
			float32 fTemp4 = float32 ((int (int ((fControl[3] == fVec0[1]))) | int (iControl[0])));
			fRec20[0] = ((0.999000013f * (fTemp4 * fRec20[1])) + (fControl[10] * (1.0f - (0.999000013f * fTemp4))));
			float32 fTemp5 = (fConst11 * ((fControl[9] / (fRec20[0] * ((fControl[6] * (fRec15[0] * fTemp1)) + 1.0f))) + -0.0500000007f));
			float32 fTemp6 = (fTemp5 + -1.49999499f);
			int32 iTemp7 = int32 (fTemp6);
			int32 iTemp8 = (int32 (min (fConst10, float32 (max (0, int32 (iTemp7))))) + 1);
			float32 fTemp9 = floor (fTemp6);
			float32 fTemp10 = (fTemp5 + (-1.0f - fTemp9));
			float32 fTemp11 = (0.0f - fTemp10);
			float32 fTemp12 = (fTemp5 + (-2.0f - fTemp9));
			float32 fTemp13 = (0.0f - (0.5f * fTemp12));
			float32 fTemp14 = (fTemp5 + (-3.0f - fTemp9));
			float32 fTemp15 = (0.0f - (0.333333343f * fTemp14));
			float32 fTemp16 = (fTemp5 + (-4.0f - fTemp9));
			float32 fTemp17 = (0.0f - (0.25f * fTemp16));
			float32 fTemp18 = (fTemp5 - fTemp9);
			int32 iTemp19 = (int32 (min (fConst10, float32 (max (0, int32 ((iTemp7 + 1)))))) + 1);
			float32 fTemp20 = (0.0f - fTemp12);
			float32 fTemp21 = (0.0f - (0.5f * fTemp14));
			float32 fTemp22 = (0.0f - (0.333333343f * fTemp16));
			int32 iTemp23 = (int32 (min (fConst10, float32 (max (0, int32 ((iTemp7 + 2)))))) + 1);
			float32 fTemp24 = (0.0f - fTemp14);
			float32 fTemp25 = (0.0f - (0.5f * fTemp16));
			float32 fTemp26 = (fTemp10 * fTemp12);
			int32 iTemp27 = (int32 (min (fConst10, float32 (max (0, int32 ((iTemp7 + 3)))))) + 1);
			float32 fTemp28 = (0.0f - fTemp16);
			float32 fTemp29 = (fTemp26 * fTemp14);
			int32 iTemp30 = (int32 (min (fConst10, float32 (max (0, int32 ((iTemp7 + 4)))))) + 1);
			fVec1[0] = (((((fRec14.at ((int ((IOTA - iTemp8)) & int (2047))) * fTemp11) * fTemp13) * fTemp15) * fTemp17) + (fTemp18 * ((((((fRec14.at ((int ((IOTA - iTemp19)) & int (2047))) * fTemp20) * fTemp21) * fTemp22) + (0.5f * (((fTemp10 * fRec14.at ((int ((IOTA - iTemp23)) & int (2047)))) * fTemp24) * fTemp25))) + (0.166666672f * ((fTemp26 * fRec14.at ((int ((IOTA - iTemp27)) & int (2047)))) * fTemp28))) + (0.0416666679f * (fTemp29 * fRec14.at ((int ((IOTA - iTemp30)) & int (2047))))))));
			fRec11[0] = fVec1[1];
			float32 fRec7 = fRec10;
			float32 fRec8 = fRec11[0];
			float32 fRec9 = fRec11[0];
			fRec2.at ((int (IOTA) & int (2047))) = fRec7;
			float32 fRec3 = (((((fTemp11 * fTemp13) * fTemp15) * fTemp17) * fRec2.at ((int ((IOTA - iTemp8)) & int (2047)))) + (fTemp18 * ((((((fTemp20 * fTemp21) * fTemp22) * fRec2.at ((int ((IOTA - iTemp19)) & int (2047)))) + (0.5f * (((fTemp10 * fTemp24) * fTemp25) * fRec2.at ((int ((IOTA - iTemp23)) & int (2047)))))) + (0.166666672f * ((fTemp26 * fTemp28) * fRec2.at ((int ((IOTA - iTemp27)) & int (2047)))))) + (0.0416666679f * (fTemp29 * fRec2.at ((int ((IOTA - iTemp30)) & int (2047))))))));
			float32 fRec4 = fRec8;
			float32 fRec5 = fRec9;
			fRec0[0] = fRec3;
			float32 fRec1 = fRec5;
			float32 fTemp31 = (fControl[0] * fRec1);
			output0 << float32 (fTemp31);
			output1 << float32 (fTemp31);
			iRec6[1] = iRec6[0];
			fRec12[1] = fRec12[0];
			fVec0[1] = fVec0[0];
			fRec15[1] = fRec15[0];
			iRec17[1] = iRec17[0];
			fRec16[2] = fRec16[1];
			fRec16[1] = fRec16[0];
			fRec19[1] = fRec19[0];
			fRec13[1] = fRec13[0];
			IOTA = (IOTA + 1);
			fRec20[1] = fRec20[0];
			fVec1[1] = fVec1[0];
			fRec11[1] = fRec11[0];
			fRec0[1] = fRec0[0];

			// Moves all streams forward by one ''tick''
			advance();
		}
	}
}

// Decode MIDI events and send freq, gain, gate values to the processor
processor dsp_wrapper
{
    input event
    {
        soul::note_events::NoteOn    noteOn;
        soul::note_events::NoteOff   noteOff;
        soul::note_events::PitchBend pitchBend;
    }

    output event float32 freq, gain, gate;

    event noteOn (soul::note_events::NoteOn e)
    {
    	freq << soul::noteNumberToFrequency (e.note);
        gain << e.velocity;
        gate << 1.0f;
    }

    event noteOff (soul::note_events::NoteOff e)
    {
        gate << 0.0f;
    }

    event pitchBend (soul::note_events::PitchBend e)
    {
    	// TODO
    }

    void run()
    {
        advance();
    }
}

/*
    Connect the wrapper with the real processor.
    This indirection is done since the "expose control event" model is not yet in place.
    So we have some ''ad-hoc'' code for now that may change in the future
*/

graph dsp_voice
{
    input event
    {
        soul::note_events::NoteOn   	 noteOn;
        soul::note_events::NoteOff  	 noteOff;
        soul::note_events::PitchBend  pitchBend;
    }

    output stream float audioOut0;
    output stream float audioOut1;

    connection
    {
        noteOn    -> dsp_wrapper.noteOn;
        noteOff   -> dsp_wrapper.noteOff;
        pitchBend -> dsp_wrapper.pitchBend;

        // This is still the ''hard coded'' part
        dsp_wrapper.freq  -> clarinetMIDI.event_freq;
        dsp_wrapper.gain  -> clarinetMIDI.event_gain;
        dsp_wrapper.gate  -> clarinetMIDI.event_gate;

        clarinetMIDI.output0 ->  audioOut0;
        clarinetMIDI.output1 ->  audioOut1;
    }
}

processor RoundRobinVoiceAllocator (int voiceCount)
{
    input event
    {
        soul::note_events::NoteOn     noteOn;
        soul::note_events::NoteOff    noteOff;
        soul::note_events::PitchBend  pitchBend;
    }

    output event
    {
        soul::note_events::NoteOn     voiceNoteOn[voiceCount];
        soul::note_events::NoteOff    voiceNoteOff[voiceCount];
        soul::note_events::PitchBend  voicePitchBend[voiceCount];
    }

    event noteOn (soul::note_events::NoteOn e)
    {
        wrap<voiceCount> allocatedVoice = 0;
        var allocatedVoiceAge = voiceInfo[allocatedVoice].voiceAge;

        // Find the oldest voice to reuse
        for (int i = 1; i < voiceCount; i++)
        {
            let age = voiceInfo.at (i).voiceAge;

            if (age < allocatedVoiceAge)
            {
                allocatedVoiceAge = age;
                allocatedVoice = wrap<voiceCount>(i);
            }
        }

        // Update the VoiceInfo for our chosen voice
        voiceInfo[allocatedVoice].channel  = e.channel;
        voiceInfo[allocatedVoice].note     = e.note;
        voiceInfo[allocatedVoice].voiceAge = nextAllocatedVoiceAge++;

        // Send the note on to the voice
        voiceNoteOn[allocatedVoice] << e;
    }

    event noteOff (soul::note_events::NoteOff e)
    {
        // Release all voices associated with this note/channel
        wrap<voiceCount> voice = 0;

        loop (voiceCount)
        {
            if (voiceInfo[voice].channel == e.channel
                 && voiceInfo[voice].note == e.note)
            {
                // Mark the voice as being unused
                voiceInfo[voice].voiceAge = nextUnallocatedVoiceAge++;
                voiceNoteOff[voice] << e;
            }

            ++voice;
        }
    }

    event pitchBend (soul::note_events::PitchBend e)
    {
        // Forward the pitch bend to all notes on this channel
        wrap<voiceCount> voice = 0;

        loop (voiceCount)
        {
            if (voiceInfo[voice].channel == e.channel)
                voicePitchBend[voice] << e;

            voice++;
        }
    }

    struct VoiceInfo
    {
        bool active;
        float note;
        int channel, voiceAge;
    }

    int nextAllocatedVoiceAge   = 1000000000;
    int nextUnallocatedVoiceAge = 1;

    VoiceInfo[voiceCount] voiceInfo;

    void run()
    {
        loop { advance(); }
    }
}

// Allocates voices, does dynamic voice management, connect to MIDI parser
graph clarinetMIDI_poly [[ main ]]
{
    input event soul::midi::Message midiIn;
    output stream float audioOut0;
    output stream float audioOut1;

    let
    {
        midiParser = soul::midi::MPEParser;
        voices = dsp_voice[16];
        voiceAllocator = RoundRobinVoiceAllocator (16);
    }

    connection
    {
        midiIn -> midiParser.parseMIDI;

        midiParser.eventOut -> voiceAllocator.noteOn,
                               voiceAllocator.noteOff,
                               voiceAllocator.pitchBend;

        // Plumb the voice allocator to the voices array
        voiceAllocator.voiceNoteOn    -> voices.noteOn;
        voiceAllocator.voiceNoteOff   -> voices.noteOff;
        voiceAllocator.voicePitchBend -> voices.pitchBend;

        // Sum the voices audio out to the output
        voices.audioOut0 -> audioOut0;
        voices.audioOut1 -> audioOut1;
    }
}
', 'clarinetMIDI.soul',
        (select id from soulpatches where name = 'clarinetMIDI'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOULPATCH', '{
	"soulPatchV1":
	{
		"ID": "grame.soul.clarinetMIDI",
		"version": "1.0",
		"name": "clarinetMIDI",
		"description": "SOUL example",
		"category": "synth",
		"manufacturer": "GRAME",
		"website": "https://faust.grame.fr",
		"isInstrument": true,
		"source": "clarinetMIDI.soul"
	}
}',
        'clarinetMIDI.soulpatch',
        (select id from soulpatches where name = 'clarinetMIDI'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOUL', '/*
    == SOUL example code ==

    Plays the first few notes of Gran Vals by Francisco Tarrega, using a
    sequence of sine-wave tones.
*/

processor ClassicRingtone
{
    output stream float out;

    void run()
    {
        int[] pitches    = (76, 74, 66, 68, 73, 71, 62, 64, 71, 69, 61, 64, 69);
        int[] durations  = ( 1,  1,  2,  2,  1,  1,  2,  2,  1,  1,  2,  2,  4);

        for (int i = 0; i < pitches.size; ++i)
            playNote (pitches.at (i), durations.at (i));
    }

    float sinewavePhase;

    void playNote (int pitch, int lengthInQuarterNotes)
    {
        let samplesPerQuarterNote = int (processor.frequency / 7);

        let noteFrequency  = soul::noteNumberToFrequency (pitch);
        let noteLength     = samplesPerQuarterNote * lengthInQuarterNotes;
        let phaseIncrement = float (noteFrequency * twoPi * processor.period);

        loop (noteLength)
        {
            out << 0.1f * sin (sinewavePhase);
            sinewavePhase = addModulo2Pi (sinewavePhase, phaseIncrement);
            advance();
        }
    }
}
', 'ClassicRingtone.soul',
        (select id from soulpatches where name = 'ClassicRingtone'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOULPATCH', '{
    "soulPatchV1":
    {
        "ID":               "dev.soul.examples.classicringtone",
        "version":          "1.0",
        "name":             "Classic Ringtone",
        "description":      "SOUL Classic Ringtone",
        "category":         "generator",
        "manufacturer":     "soul.dev",
        "isInstrument":     false,

        "source":           "ClassicRingtone.soul"
    }
}',
        'soulpatchfile 1.soulpatch',
        (select id from soulpatches where name = 'ClassicRingtone'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOUL', '/*
    == SOUL example code ==

    A simple implementation of a delay, where the length and feedback level can
    be dynamically set using event parameters.
*/

processor Delay  [[ main ]]
{
    input  stream float audioIn;
    output stream float audioOut;

    input event
    {
        float delayLength    [[ min: 0,  max: 1000, init:  50,  name: "Length",   unit: "ms", step: 1 ]];
        float delayFeedback  [[ min: 0,  max: 100,  init:  25,  name: "Feedback", unit: "%",  step: 1 ]];
    }

    event delayLength (float delayMs)
    {
        let delaySamples = max (1, int (processor.frequency * (delayMs / 1000.0f)));

        readPos = writePos - delaySamples;
    }

    event delayFeedback (float f)
    {
        feedback = f * 0.01f;
    }

    let maxDelayLength = 100000;

    float feedback;
    float[maxDelayLength] buffer;
    wrap<maxDelayLength> readPos, writePos;

    void run()
    {
        loop
        {
            buffer[writePos] = audioIn + feedback * buffer[readPos];
            audioOut << buffer[readPos];

            ++readPos;
            ++writePos;

            advance();
        }
    }
}
', 'Delay.soul',
        (select id from soulpatches where name = 'Delay'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOULPATCH', '{
  "soulPatchV1": {
    "ID": "dev.soul.examples.delay",
    "version": "1.0",
    "name": "Delay",
    "description": "SOUL Delay",
    "category": "fx",
    "manufacturer": "soul.dev",
    "isInstrument": false,
    "source": "Delay.soul"
  }
}',
        'Delay.soulpatch',
        (select id from soulpatches where name = 'Delay'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOUL', '/*
    == SOUL example code ==
    == Designed by Ivan COHEN ==

    This is an algorithm simulating a Diode Clipper, which is an analog low-pass
    filter with a resistance, a capacitance, and two parallel diodes put in
    opposite directions. The linear part is simulated using the TPT structure,
    and the nonlinear part uses the Newton-Raphson''s method to solve the roots
    of the implicit equation.
*/

graph Diode  [[ main ]]
{
    input  stream float audioIn;    // just using mono in and out
    output stream float audioOut;

    input event float cutoffFrequency   [[ min: 20,  max: 20000,  init: 10000,  step: 10    ]];
    input event float gaindB            [[ min:  0,  max:    40,  init:    20,  step:  0.1  ]];

    let diodeClipper = DiodeClipper * 4;  // oversampling 4 times

    connection
    {
        audioIn         -> diodeClipper.audioIn;
        cutoffFrequency -> diodeClipper.cutoffFrequencyIn;
        gaindB          -> diodeClipper.gaindBIn;

        diodeClipper.audioOut -> audioOut;
    }
}

//==============================================================================
processor DiodeClipper
{
    input  stream float audioIn;
    output stream float audioOut;

    input event float cutoffFrequencyIn;
    input event float gaindBIn;

    event cutoffFrequencyIn (float f)   { cutoffFrequency = f; }
    event gaindBIn (float f)            { gaindB = f; }

    // Diode Clipper parameters
    float cutoffFrequency = 10000.0f;
    float gaindB = 40.0f;

    // filter variables
    float G, gain;

    // Main processing function
    void run()
    {
        // internal constants (1N4148)
        let Is          = 2.52e-9;
        let mu          = 1.752;
        let Vt          = 26e-3;
        let R           = 2.2e3;
        let tolerance   = 1e-12;

        // state variables
        float32 s1;
        float64 out;

        let updateInterval = 8;   // number of samples between calls to updateFilterVariables()
        let maxNewtonRaphsonIterations = 64;

        loop
        {
            updateFilterVariables();

            // DAFX15 Capped Step
            let deltaLim = mu * Vt * acosh (mu * Vt / 2.0 / (R * Is * G));

            loop (updateInterval)
            {
                let in = audioIn * gain;
                let p = G * (in - s1) + s1;
                float64 delta = 1e9;

                loop (maxNewtonRaphsonIterations)
                {
                    if (abs (delta) <= tolerance)
                        break;

                    let J = p - (2 * G * R * Is) * sinh (out / (mu * Vt)) - out;
                    let dJ = -1 - G * 2 * R * Is / (mu * Vt) * cosh (out / (mu * Vt));

                    // DAFX15 Capped Step
                    delta = clamp (-J / dJ, -deltaLim, deltaLim);

                    // next iteration
                    out += delta;
                }

                // TPT structure updates
                let v = float (out) - s1;
                s1 = float (out) + v;

                audioOut << float (out);
                advance();
            }
        }
    }

    void updateFilterVariables()
    {
        // low-pass filter internal variables update
        let cutoff = clamp (cutoffFrequency, 10.0f, float32 (processor.frequency) * 0.49999f);
        let g = tan (float (pi) * cutoff / float32 (processor.frequency));
        G = g / (1 + g);

        // gain update
        gain = soul::dBtoGain (gaindB);
    }
}
', 'DiodeClipper.soul',
        (select id from soulpatches where name = 'DiodeClipper'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOULPATCH', '{
    "soulPatchV1":
    {
        "ID":               "dev.soul.examples.diodeclipper",
        "version":          "1.0",
        "name":             "Diode Clipper",
        "description":      "SOUL Diode Clipper",
        "category":         "fx",
        "manufacturer":     "soul.dev",
        "isInstrument":     false,

        "source":           "DiodeClipper.soul"
    }
}',
        'DiodeClipper.soulpatch',
        (select id from soulpatches where name = 'DiodeClipper'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOUL', '/*
    == SOUL example code ==

    "Minimum-Viable-Piano"

    You probably won''t want to use this patch to replace your Bosendorfer,
    but it shows how to play some re-pitched external sample data, and
    it''ll do a pretty good job of recreating some early 1990s rave piano.

    In memory of Philip Meehan, who recorded these piano samples for use in
    the early Tracktion demo songs, back in around 2001.
*/

graph Piano  [[ main ]]
{
    input event float volume [[ min: -40, max: 0,  init: -6, unit: "dB", step: 1, label: "Volume" ]];
    input event soul::midi::Message midiIn;
    output stream float audioOut;

    let
    {
        midiParser      = soul::midi::MPEParser;
        voices          = Voice[8];
        voiceAllocator  = soul::voice_allocators::Basic(8);
        gainParameter   = GainParameterRamp (1.0f);
        masterVolume    = soul::gain::DynamicGain (float);
    }

    connection
    {
        midiIn -> midiParser.parseMIDI;
        volume -> gainParameter.gainDb;

        midiParser.eventOut -> voiceAllocator.eventIn;

        // Plumb the voice allocator to the voices array
        voiceAllocator.voiceEventOut -> voices.eventIn;

        // Sum the voices audio out to the output
        voices.audioOut -> masterVolume.in;
        gainParameter.parameterOut -> masterVolume.gain;
        masterVolume.out -> audioOut;
    }
}

//==============================================================================
processor PianoSamplePlayer
{
    input event (soul::note_events::NoteOn,
                 soul::note_events::NoteOff) eventIn;

    output stream float audioOut;

    external soul::audio_samples::Mono  piano_C5,
                                        piano_G5,
                                        piano_C6,
                                        piano_G6;

    struct Sample
    {
        soul::audio_samples::Mono audioData;
        int rootNote;
    }

    Sample[4] samples;

    float[] sourceFrames;
    float64 playbackPosition, positionIncrement;

    event eventIn (soul::note_events::NoteOn e)
    {
        let sample = findBestSampleForNote (int (e.note));
        sourceFrames = sample.audioData.frames;
        positionIncrement = soul::getSpeedRatioForPitchedSample (sample.audioData.sampleRate,
                                                                 float (sample.rootNote),
                                                                 processor.frequency, e.note);
        playbackPosition = 0;
    }

    event eventIn (soul::note_events::NoteOff e)  {}

    void initialiseSamples()
    {
        samples = ((piano_C5, 72),
                   (piano_G5, 79),
                   (piano_C6, 84),
                   (piano_G6, 91));
    }

    Sample findBestSampleForNote (int targetNote)
    {
        int bestDistance = 128;
        int bestIndex = 0;

        for (int i  = 0; i < samples.size; ++i)
        {
            let distance = abs (samples.at(i).rootNote - targetNote);

            if (distance < bestDistance)
            {
                bestDistance = distance;
                bestIndex = i;
            }
        }

        return samples.at (bestIndex);
    }

    void run()
    {
        initialiseSamples();

        loop
        {
            if (positionIncrement > 0)
            {
                audioOut << sourceFrames.readLinearInterpolated (playbackPosition);

                playbackPosition += positionIncrement;

                if (playbackPosition >= sourceFrames.size)
                    positionIncrement = 0;  // stop at the end of the sample
            }

            advance();
        }
    }
}

//==============================================================================
graph Voice
{
    input event (soul::note_events::NoteOn,
                 soul::note_events::NoteOff) eventIn;

    output stream float audioOut;

    let
    {
        samplePlayer = PianoSamplePlayer;
        envelope = soul::envelope::FixedAttackReleaseEnvelope (1.0f, 0.0f, 0.3f);
        attenuator = soul::gain::DynamicGain (float);
    }

    connection
    {
        eventIn -> samplePlayer.eventIn,
                   envelope.noteIn;

        samplePlayer.audioOut  -> attenuator.in;
        envelope.levelOut      -> attenuator.gain;

        attenuator.out -> audioOut;
    }
}

//==============================================================================
// Converts an input event (as a gain in db) to a ramped stream
processor GainParameterRamp (float slewRate)
{
    input event float gainDb;
    output stream float parameterOut;

    event gainDb (float targetDb)
    {
        targetValue = soul::dBtoGain (targetDb);

        let diff = targetValue - currentValue;
        let rampSeconds = abs (diff) / slewRate;

        rampSamples   = int (processor.frequency * rampSeconds);
        rampIncrement = diff / float (rampSamples);
    }

    float targetValue;
    float currentValue;
    float rampIncrement;
    int rampSamples;

    void run()
    {
        loop
        {
            if (rampSamples > 0)
            {
                currentValue += rampIncrement;
                --rampSamples;
            }

            parameterOut << currentValue;
            advance();
        }
    }
}
', 'MinimumViablePiano.soul',
        (select id from soulpatches where name = 'MinimumViablePiano'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOULPATCH', '{
    "soulPatchV1":
    {
        "ID":               "dev.soul.examples.MinimumViablePiano",
        "version":          "1.0",
        "name":             "Minimum Viable Piano",
        "description":      "A piano demo, designed for small size rather than high quality",
        "category":         "synth",
        "manufacturer":     "soul.dev",
        "isInstrument":     true,

        "source":           "MinimumViablePiano.soul",

        "externals":
        {
            "PianoSamplePlayer::piano_C5": "piano-C5.ogg",
            "PianoSamplePlayer::piano_G5": "piano-G5.ogg",
            "PianoSamplePlayer::piano_C6": "piano-C6.ogg",
            "PianoSamplePlayer::piano_G6": "piano-G6.ogg"
        }
    }
}',
        'MinimumViablePiano.soulpatch',
        (select id from soulpatches where name = 'MinimumViablePiano'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOUL', '/*
    == SOUL example code ==

    A fairly complex expressive synthesiser with some effects and
    dynamic parameters.
*/

graph PadSynth  [[main]]
{
    input
    {
        event soul::midi::Message midiIn;

        oscillators.cutoffParam           [[ name: "Cutoff",                min: 0,       max: 127,    init: 80,    unit: "semi",   step: 0.1   ]];
        oscillators.resonanceParam        [[ name: "Resonance",             min: 0,       max: 100,    init: 20,    unit: "%",      step: 1     ]];
        oscillators.ampAttack             [[ name: "Amp Attack",            min: 0,       max: 100,    init: 80,    unit: "%",      step: 1     ]];
        oscillators.ampDecay              [[ name: "Amp Decay",             min: 0,       max: 100,    init: 66,    unit: "%",      step: 1     ]];
        oscillators.ampSustain            [[ name: "Amp Sustain",           min: 0,       max: 100,    init: 100,   unit: "%",      step: 1     ]];
        oscillators.ampRelease            [[ name: "Amp Release",           min: 0,       max: 100,    init: 88,    unit: "%",      step: 1     ]];
        oscillators.ampSensitivity        [[ name: "Amp Sensitivity",       min: 0,       max: 100,    init: 50,    unit: "%",      step: 1     ]];
        oscillators.osc1waveshape         [[ name: "Osc1 Waveshape",        min: 0,       max: 100,    init: 100,   unit: "%",      step: 1     ]];
        oscillators.osc1detuneSemitones   [[ name: "Osc1 Detune",           min: -24,     max: 24,     init: 0,     unit: "semi",   step: 1     ]];
        oscillators.osc1detune            [[ name: "Osc1 Detune",           min: -100,    max: 100,    init: 0,     unit: "cent",   step: 1     ]];
        oscillators.osc2waveshape         [[ name: "Osc2 Waveshape",        min: 0,       max: 100,    init: 1.0,   unit: "%",      step: 1     ]];
        oscillators.osc2detuneSemitones   [[ name: "Osc2 Detune",           min: -24,     max: 24,     init: 0,     unit: "semi",   step: 1     ]];
        oscillators.osc2detune            [[ name: "Osc2 Detune",           min: -100,    max: 100,    init: 1,     unit: "cent",   step: 1     ]];
        oscillators.oscillatorMix         [[ name: "Osc Mix",               min: 0,       max: 100,    init: 22,    unit: "%",      step: 1     ]];
        oscillators.filterAttack          [[ name: "Filter Attack",         min: 0,       max: 100,    init: 80,    unit: "%",      step: 1     ]];
        oscillators.filterDecay           [[ name: "Filter Decay",          min: 0,       max: 100,    init: 74,    unit: "%",      step: 1     ]];
        oscillators.filterSustain         [[ name: "Filter Sustain",        min: 0,       max: 100,    init: 75,    unit: "%",      step: 1     ]];
        oscillators.filterRelease         [[ name: "Filter Release",        min: 0,       max: 100,    init: 66,    unit: "%",      step: 1     ]];
        oscillators.filterSensitivity     [[ name: "Filter Sensitivity",    min: 0,       max: 100,    init: 25,    unit: "%",      step: 1     ]];
        oscillators.filterEnvMod          [[ name: "Filter Env Mod",        min: -24,     max: 24,     init: 12,    unit: "semi",   step: 1     ]];
        oscillators.filterCutoffTrack     [[ name: "Filter Cutoff Track",   min: 0,       max: 1,      init: 1,     unit: "semi",   step: 0.01  ]];
        dryWetMixer.mix  dryWetMix        [[ name: "Dry Wet Mix",           min: 0,       max: 100,    init: 100,   unit: "%",      step: 1     ]];
    }

    output stream float audioOut;

    let
    {
        delay = Delay (20000, 0.5f);
        oscillators = Oscillators;
        dryWetMixer = soul::mixers::DynamicMix (float, 100.0f);
    }

    connection
    {
        midiIn               -> oscillators.midiIn;

        oscillators.audioOut -> delay.audioIn;
        delay.audioOut       -> Reverb.audioIn;

        oscillators.audioOut -> dryWetMixer.in1;
        Reverb.audioOut      -> dryWetMixer.in2;
        dryWetMixer.out      -> audioOut;
    }
}

//==============================================================================
namespace NoteHandler
{
    struct NoteInfo
    {
        int channel;
        float bendSemitones;
        float note;
        float pressure;
        float slide;
    }

    struct NoteHandler
    {
        float currentVelocity;
        float currentNote;
        float currentBendSemitones;
        float currentPressure;
        float currentSlide;

        int maxNotes;

        NoteInfo[16] noteArray;
        wrap<16> activeNotes;
    }

    NoteHandler create()
    {
        NoteHandler r;
        r.currentVelocity = 0;
        r.currentNote = 0;
        r.activeNotes = 0;
        r.maxNotes = 16;
        return r;
    }

    void addNote (NoteHandler& this, int channel, float note, float velocity)
    {
        // Initial note velocity is used
        if (int(this.activeNotes) == 0)
            this.currentVelocity = velocity;

        if (int(this.activeNotes) < this.maxNotes)
        {
            this.noteArray[this.activeNotes].channel = channel;
            this.noteArray[this.activeNotes].note = note;
            this.noteArray[this.activeNotes].bendSemitones = 0;
            this.activeNotes++;
        }

        this.currentNote = note;
        this.currentBendSemitones = 0;
        this.currentPressure = 0;
        this.currentSlide = 0;
    }

    void removeNote (NoteHandler& this, int channel, float note)
    {
        wrap<this.noteArray.size> readPoint = 0;
        wrap<this.noteArray.size> writePoint = 0;

        while (readPoint < int(this.activeNotes))
        {
            if (this.noteArray[readPoint].note != note)
            {
                this.noteArray[writePoint] = this.noteArray[readPoint];
                ++writePoint;
            }

            ++readPoint;
        }

        this.activeNotes = writePoint;

        // Update the current active note
        if (int(this.activeNotes) > 0)
        {
            this.currentNote = this.noteArray[this.activeNotes - 1].note;
            this.currentBendSemitones = this.noteArray[this.activeNotes - 1].bendSemitones;
        }
    }

    void addPitchBend (NoteHandler& this, int channel, float bendSemitones)
    {
        // Update all notes on this channel
        for (int i = 0; i < int(this.activeNotes); ++i)
            if (this.noteArray.at(i).channel == channel)
                this.noteArray.at(i).bendSemitones = bendSemitones;

        if (int(this.activeNotes) > 0)
            this.currentBendSemitones = this.noteArray[this.activeNotes - 1].bendSemitones;
    }

    void addPressure (NoteHandler& this, int channel, float pressure)
    {
        for (int i = 0; i < int(this.activeNotes); ++i)
            if (this.noteArray.at(i).channel == channel)
                this.noteArray.at(i).pressure = pressure;

        if (int(this.activeNotes) > 0)
            this.currentPressure = this.noteArray[this.activeNotes - 1].pressure;
    }

    void addSlide (NoteHandler& this, int channel, float slide)
    {
        for (int i = 0; i < int(this.activeNotes); ++i)
            if (this.noteArray.at(i).channel == channel)
                this.noteArray.at(i).slide = slide;

        if (int(this.activeNotes) > 0)
            this.currentSlide= this.noteArray[this.activeNotes - 1].slide;
    }

    float getCurrentNote (NoteHandler& this)        { return this.currentNote + this.currentBendSemitones; }
    float getCurrentVelocity (NoteHandler& this)    { return this.currentVelocity; }
    float getCurrentPressure (NoteHandler& this)    { return this.currentPressure; }
    float getCurrentSlide (NoteHandler& this)       { return this.currentSlide; }

    bool isNoteActive (NoteHandler& this)           { return int(this.activeNotes) != 0; }
}

//==============================================================================
/// PolyBlep oscillator - variable waveshape between 0.0 (saw) and 1.0 (square)
processor PolyBlep
{
    input event float waveshape, detuneSemitones, detune;
    input stream float frequencyIn;
    output stream float audioOut;

    event waveshape (float f)
    {
        waveshapeValue = f * 0.01f;
    }

    event detuneSemitones (float f)
    {
        detuneSemitonesValue = f;
        frequencyMultiplier = pow (2.0f, (detuneSemitonesValue + detuneValue) / 12.0f);
    }

    event detune (float f)
    {
        detuneValue = f * 0.01f;
        frequencyMultiplier = pow (2.0f, (detuneSemitonesValue + detuneValue) / 12.0f);
    }

    let frequencyScaling = 1.0f / float(processor.frequency);

    float normalisedPhase = 0.0f;
    float normalisedPhaseIncrement = 0.0f;

    float waveshapeValue = 0.0f;
    float detuneSemitonesValue = 0.0f;
    float detuneValue = 0.0f;
    float frequencyMultiplier = 1.0f;

    float blep (float t2)
    {
        float t = t2;

        if (t < normalisedPhaseIncrement)
        {
            t /= normalisedPhaseIncrement;
            return (t + t) - (t * t) - 1.0f;
        }

        if (t > (1.0f - normalisedPhaseIncrement))
        {
            t = (t - 1.0f) / normalisedPhaseIncrement;
            return (t * t) + (t + t) + 1.0f;
        }

        return 0;
    }

    void run()
    {
        loop
        {
            // Generate phase increment from our input frequency
            normalisedPhaseIncrement = frequencyIn * frequencyScaling * frequencyMultiplier;

            while (normalisedPhase >= 1.0f)
                normalisedPhase -= 1.0f;

            var normalisedPhase2 = normalisedPhase + 0.5f;

            if (normalisedPhase2 > 1.0f)
                normalisedPhase2 -= 1.0f;

            let sawValue = (2.0f * normalisedPhase) - 1.0f - blep (normalisedPhase);
            var squareValue = -blep (normalisedPhase) + blep (normalisedPhase2);

            squareValue += (normalisedPhase <= 0.5f) ? -1.0f : 1.0f;

            // Mix the square and saw waves together
            audioOut << squareValue * waveshapeValue + (sawValue * (1.0f - waveshapeValue));

            normalisedPhase += normalisedPhaseIncrement;
            advance();
        }
    }
}

//==============================================================================
// Runs an ADSR envelope using the input as a trigger
processor ADSREnvelope
{
    input stream float triggerLevel;
    output stream float envelopeLevel;
    input event float attack, decay, sustain, release, sensitivity;

    event attack (float f)
    {
        let seconds = 0.001 + 9.999 * pow (f * 0.01f, 4.0f);
        let secondsToLinearStep = 1.0 / (sampleRate * seconds);

        attackRamp = secondsToLinearStep;
    }

    event decay (float f)
    {
        let seconds = 0.001 + 9.999 * pow (f * 0.01f, 4.0f);
        let inverse = 1.0 / (sampleRate * seconds);
        let secondsToMultiplier = pow (0.0001, inverse);

        decayMultiplier = secondsToMultiplier;
    }

    event sustain (float f)
    {
        sustainLevel = float64 (f) * 0.01f;
    }

    event release (float f)
    {
        let seconds = 0.001 + 9.999 * pow (f * 0.01f, 4.0f);
        let inverse = 1.0 / (sampleRate * seconds);
        let secondsToMultiplier = pow (0.0001, inverse);

        releaseMultiplier = secondsToMultiplier;
    }

    event sensitivity (float f)
    {
        velocitySensitivity = f * 0.01f;
    }

    const float sampleRate = float (processor.frequency);

    float64 attackRamp = 0.01;
    float64 decayMultiplier = 0.995;
    float64 sustainLevel = 1.0;
    float64 releaseMultiplier = 0.99991;
    float32 velocitySensitivity = 0.0f;
    float64 targetValue = 1.0;

    void calculateTargetValue (float noteVelocity)
    {
        // Use the velocitySensitivity to decide how the noteVelocity affects the target volume
        // We determine a dB attenuation range, then the note velocity decides where we are on it
        // Full velocity sensitivity is -12dB

        // We use 100 as a ''loud'' note, so that''s 0.75 out of 100 as ''normal'', any higher velocity will be louder
        let attenuation = -12.0f * velocitySensitivity * (0.75f - noteVelocity);

        targetValue = pow (10.0f, attenuation / 10.0f);
    }

    void run()
    {
        float64 value = 0;

        loop
        {
            if (triggerLevel > 0)
            {
                // Use the value of the trigger to modify our target value
                calculateTargetValue (triggerLevel);

                while (value < targetValue)
                {
                    value += attackRamp;
                    envelopeLevel << float(value);
                    advance();

                    if (triggerLevel <= 0)
                        break;
                }

                // Cap it to the target value
                value = min (value, targetValue);

                // Decay stage
                while (value > targetValue * sustainLevel && triggerLevel > 0)
                {
                    value = value * decayMultiplier;
                    envelopeLevel << float (value);
                    advance();
                }

                // Sustain stage
                while (triggerLevel > 0)
                {
                    envelopeLevel << float (value);
                    advance();
                }
            }

            value *= releaseMultiplier;
            envelopeLevel << float (value);
            advance();
        }
    }
}

//==============================================================================
processor LowPassFilter
{
    input  stream float gainIn, cutoffIn, resonanceIn, audioIn;
    output stream float audioOut;

    let numTableEntries = 128;
    let maxResonance = 100;
    let samplesBetweenFactorUpdates = 64;

    struct FilterTerms
    {
        float a, b, c;
    }

    struct FrequencySettings
    {
        FilterTerms[maxResonance + 1] resonance;
    }

    struct PrecalcTerms
    {
        FrequencySettings[numTableEntries] frequency;
    }

    PrecalcTerms precalcTerms;

    FilterTerms calcTermEntry (float frequency, float resonance)
    {
        FilterTerms result;

        let r = 1.4f - (resonance * 0.0135f);
        let f = clamp (soul::noteNumberToFrequency(frequency), 0.0f, 20000.0f);
        let c = 1.0f / tan (float(pi) * (10.0f + f) / float32(processor.frequency));

        result.a = 1.0f / (1.0f + r * c + c * c);
        result.b = 2.0f * (1.0f - c * c) * result.a;
        result.c = (1.0f - r * c + c * c) * result.a;

        return result;
    }

    void initialiseTerms()
    {
        for (int i = 0; i < numTableEntries; ++i)
            for (int j = 0; j <= maxResonance; ++j)
                precalcTerms.frequency.at(i).resonance.at(j) = calcTermEntry (float(i), float(j));
    }

    void updateFactors (float cutoff, float resonance)
    {
        cutoff    = clamp (cutoff, 0.0f, float(numTableEntries - 2));
        resonance = clamp (resonance, 0.0f, float(maxResonance - 1));

        // Interpolate the cutoff
        let cutoffInterpolation = cutoff - int(cutoff);

        let resonanceEntry = wrap<maxResonance>(int(resonance));
        var cutoffEntry = wrap<numTableEntries>(int(cutoff));

        let terms1_a = precalcTerms.frequency[cutoffEntry].resonance[resonanceEntry].a;
        let terms1_b = precalcTerms.frequency[cutoffEntry].resonance[resonanceEntry].b;
        let terms1_c = precalcTerms.frequency[cutoffEntry].resonance[resonanceEntry].c;

        ++cutoffEntry;

        let terms2_a = precalcTerms.frequency[cutoffEntry].resonance[resonanceEntry].a;
        let terms2_b = precalcTerms.frequency[cutoffEntry].resonance[resonanceEntry].b;
        let terms2_c = precalcTerms.frequency[cutoffEntry].resonance[resonanceEntry].c;

        factor_a1 = terms1_a + (terms2_a - terms1_a) * cutoffInterpolation;
        factor_a2 = 2.0f * factor_a1;
        factor_b1 = terms1_b + (terms2_b - terms1_b) * cutoffInterpolation;
        factor_b2 = terms1_c + (terms2_c - terms1_c) * cutoffInterpolation;
    }

    float factor_a1, factor_a2, factor_b1, factor_b2;

    void run()
    {
        initialiseTerms();
        float in_1, in_2, out_1, out_2;

        loop
        {
            updateFactors(cutoffIn, resonanceIn);

            loop (samplesBetweenFactorUpdates)
            {
                let in = audioIn;

                let f = factor_a1 * (in + in_1)
                         + factor_a2 * in_2
                         - factor_b1 * out_1
                         - factor_b2 * out_2;

                out_2 = out_1;
                out_1 = f;
                in_2 = in_1;
                in_1 = in;

                audioOut << f * gainIn * 0.2f;
                advance();
            }
        }
    }
}

//==============================================================================
processor ParameterStream
{
    input event float parameterUpdate;
    output stream float audioOut;

    event parameterUpdate (float f)
    {
        currentValue = f;
    }

    void run()
    {
        loop
        {
            audioOut << currentValue;
            advance();
        }
    }

    float currentValue;
}

//==============================================================================
/*
    Voice control processor - receives NoteOn/NoteOff events, and generates control signals
    for our oscillators/filters
*/
processor Controller
{
    input event (soul::note_events::NoteOn,
                 soul::note_events::NoteOff,
                 soul::note_events::PitchBend,
                 soul::note_events::Pressure,
                 soul::note_events::Slide) eventIn;

    output stream float note1_noteOut, note1_frequencyOut, note1_velocity, note1_active,
                        note2_frequencyOut, note1_pressure, note1_slide;

    event eventIn (soul::note_events::NoteOn e)     { NoteHandler::addNote      (noteHandler, e.channel, e.note, e.velocity); }
    event eventIn (soul::note_events::NoteOff e)    { NoteHandler::removeNote   (noteHandler, e.channel, e.note); }
    event eventIn (soul::note_events::PitchBend e)  { NoteHandler::addPitchBend (noteHandler, e.channel, e.bendSemitones); }
    event eventIn (soul::note_events::Pressure p)   { NoteHandler::addPressure  (noteHandler, p.channel, p.pressure); }
    event eventIn (soul::note_events::Slide s)      { NoteHandler::addSlide     (noteHandler, s.channel, s.slide); }

    NoteHandler::NoteHandler noteHandler;

    void run()
    {
        float multiplier = 1.0f;
        int sampleCount = 0;

        noteHandler = NoteHandler::create();

        loop
        {
            ++sampleCount;

            if (sampleCount == 1000)
            {
                sampleCount = 0;
                multiplier += 0.001f;

                if (multiplier >= 1.01f)
                    multiplier = 1.0f;
            }

            let noteFrequency = soul::noteNumberToFrequency (NoteHandler::getCurrentNote(noteHandler));

            note1_noteOut      << NoteHandler::getCurrentNote(noteHandler);
            note1_frequencyOut << noteFrequency;
            note2_frequencyOut << noteFrequency * multiplier;
            note1_velocity     << NoteHandler::getCurrentVelocity(noteHandler);
            note1_active       << (NoteHandler::isNoteActive(noteHandler) ? NoteHandler::getCurrentVelocity(noteHandler) : 0.0f);
            note1_pressure     << NoteHandler::getCurrentPressure(noteHandler);
            note1_slide        << NoteHandler::getCurrentSlide(noteHandler);

            advance();
        }
    }
}

//==============================================================================
processor CutoffHandler
{
    input stream float cutoffIn, envelopeIn, envelopeModIn, trackModIn, noteIn, modIn;
    output stream float cutoffOut;

    void run()
    {
        // The cutoff has a base cutoff value
        // It is modified by the envelope and by key-tracking
        loop
        {
            cutoffOut << cutoffIn
                          + (envelopeIn * envelopeModIn)
                          + ((noteIn - 60.0f) * trackModIn)
                          + (modIn * 36.0f);
            advance();
        }
    }
}

//==============================================================================
graph Voice
{
    input event (soul::note_events::NoteOn,
                 soul::note_events::NoteOff,
                 soul::note_events::PitchBend,
                 soul::note_events::Pressure,
                 soul::note_events::Slide) eventIn;

    input event float cutoffParam, resonanceParam,
                      ampAttack, ampDecay, ampSustain, ampRelease, ampSensitivity,
                      osc1waveshape, osc1detuneSemitones, osc1detune,
                      osc2waveshape, osc2detuneSemitones, osc2detune,
                      filterAttack, filterDecay, filterSustain, filterRelease, filterSensitivity,
                      oscillatorMix,
                      filterEnvMod, filterCutoffTrack;

    output stream float audioOut;

    let
    {
        osc1                = PolyBlep;
        osc2                = PolyBlep;
        amplitudeEnvelope   = ADSREnvelope;
        cutoffEnvelope      = ADSREnvelope;
        voiceController     = Controller;
        voiceOscillatorMix  = soul::mixers::DynamicMix (float, 100.0f);
        voiceCutoffHandler  = CutoffHandler;
        voiceLowPassFilter  = LowPassFilter;

        cutoff              = ParameterStream;
        resonance           = ParameterStream;
        oscillatorMixParam  = ParameterStream;
        cutoffEnvMod        = ParameterStream;
        cutoffKeytrackMod   = ParameterStream;
    }

    connection
    {
        cutoffParam         -> cutoff.parameterUpdate;
        resonanceParam      -> resonance.parameterUpdate;
        ampAttack           -> amplitudeEnvelope.attack;
        ampDecay            -> amplitudeEnvelope.decay;
        ampSustain          -> amplitudeEnvelope.sustain;
        ampRelease          -> amplitudeEnvelope.release;
        ampSensitivity      -> amplitudeEnvelope.sensitivity;
        osc1waveshape       -> osc1.waveshape;
        osc1detuneSemitones -> osc1.detuneSemitones;
        osc1detune          -> osc1.detune;
        osc2waveshape       -> osc2.waveshape;
        osc2detuneSemitones -> osc2.detuneSemitones;
        osc2detune          -> osc2.detune;
        filterAttack        -> cutoffEnvelope.attack;
        filterDecay         -> cutoffEnvelope.decay;
        filterSustain       -> cutoffEnvelope.sustain;
        filterRelease       -> cutoffEnvelope.release;
        filterSensitivity   -> cutoffEnvelope.sensitivity;
        oscillatorMix       -> oscillatorMixParam.parameterUpdate;
        filterEnvMod        -> cutoffEnvMod.parameterUpdate;
        filterCutoffTrack   -> cutoffKeytrackMod.parameterUpdate;

        eventIn -> voiceController.eventIn;

        voiceController.note1_frequencyOut -> osc1.frequencyIn;
        voiceController.note2_frequencyOut -> osc2.frequencyIn;
        voiceController.note1_active       -> amplitudeEnvelope.triggerLevel,
                                              cutoffEnvelope.triggerLevel;

        osc1.audioOut                -> voiceOscillatorMix.in1;
        osc2.audioOut                -> voiceOscillatorMix.in2;
        oscillatorMixParam.audioOut  -> voiceOscillatorMix.mix;

        cutoff.audioOut                     -> voiceCutoffHandler.cutoffIn;
        cutoffEnvelope.envelopeLevel        -> voiceCutoffHandler.envelopeIn;
        cutoffEnvMod.audioOut               -> voiceCutoffHandler.envelopeModIn;
        cutoffKeytrackMod.audioOut          -> voiceCutoffHandler.trackModIn;
        voiceController.note1_noteOut       -> voiceCutoffHandler.noteIn;
        voiceController.note1_slide         -> voiceCutoffHandler.modIn;

        voiceOscillatorMix.out          -> voiceLowPassFilter.audioIn;
        voiceCutoffHandler.cutoffOut    -> voiceLowPassFilter.cutoffIn;
        resonance.audioOut              -> voiceLowPassFilter.resonanceIn;
        amplitudeEnvelope.envelopeLevel -> voiceLowPassFilter.gainIn;

        voiceLowPassFilter.audioOut -> audioOut;
    }
}

//==============================================================================
graph Reverb
{
    input  stream float audioIn;
    output dryWetMixer.out audioOut;

    let
    {
        allPass_225 = AllPassFilter(225);
        allPass_341 = AllPassFilter(341);
        allPass_441 = AllPassFilter(441);
        allPass_556 = AllPassFilter(556);

        comb_1116 = CombFilter(1116);
        comb_1188 = CombFilter(1188);
        comb_1277 = CombFilter(1277);
        comb_1356 = CombFilter(1356);
        comb_1422 = CombFilter(1422);
        comb_1491 = CombFilter(1491);
        comb_1557 = CombFilter(1557);
        comb_1617 = CombFilter(1617);

        dryWetMixer = soul::mixers::FixedSum (float, 0.8f, 0.01485f);
    }

    connection
    {
        audioIn -> comb_1116.audioIn,
                   comb_1188.audioIn,
                   comb_1277.audioIn,
                   comb_1356.audioIn,
                   comb_1422.audioIn,
                   comb_1491.audioIn,
                   comb_1557.audioIn,
                   comb_1617.audioIn,
                   dryWetMixer.in1;

        comb_1116.audioOut,
        comb_1188.audioOut,
        comb_1277.audioOut,
        comb_1356.audioOut,
        comb_1422.audioOut,
        comb_1491.audioOut,
        comb_1557.audioOut,
        comb_1617.audioOut  -> allPass_225.audioIn;

        allPass_225.audioOut -> allPass_341.audioIn;
        allPass_341.audioOut -> allPass_441.audioIn;
        allPass_441.audioOut -> allPass_556.audioIn;
        allPass_556.audioOut -> dryWetMixer.in2;
    }
}

//==============================================================================
processor AllPassFilter (int bufferLength)
{
    input  stream float audioIn;
    output stream float audioOut;

    void run()
    {
        float[bufferLength] buffer;
        wrap<buffer.size> bufferIndex;

        loop
        {
            let bufferedValue = buffer[bufferIndex];
            buffer[bufferIndex++] = audioIn + (bufferedValue * 0.5f);

            audioOut << bufferedValue - audioIn;
            advance();
        }
    }
}

//==============================================================================
processor CombFilter (int bufferLength)
{
    input  stream float audioIn;
    output stream float audioOut;

    void run()
    {
        float[bufferLength] buffer;
        wrap<buffer.size> bufferIndex;

        let damp          = 0.2f;
        let feedbackLevel = 0.95f;

        float last;

        loop
        {
            let out = buffer[bufferIndex];
            last = (out * (1.0f - damp)) + (last * damp);
            buffer[bufferIndex++] = audioIn + (last * feedbackLevel);

            audioOut << out;
            advance();
        }
    }
}

//==============================================================================
processor Delay (int delay, float feedbackLevel)
{
    input  stream float audioIn;
    output stream float audioOut;

    float[delay] delayBuffer;

    void run()
    {
        wrap<delayBuffer.size> pos;

        loop
        {
            delayBuffer[pos] = delayBuffer[pos] * feedbackLevel + audioIn;
            audioOut << delayBuffer[pos];

            ++pos;
            advance();
        }
    }
}

//==============================================================================
graph Oscillators
{
    input midiParser.parseMIDI midiIn;

    input event float cutoffParam, resonanceParam,
                      ampAttack, ampDecay, ampSustain, ampRelease, ampSensitivity,
                      osc1waveshape, osc1detuneSemitones, osc1detune,
                      osc2waveshape, osc2detuneSemitones, osc2detune,
                      filterAttack, filterDecay, filterSustain, filterRelease, filterSensitivity,
                      oscillatorMix,
                      filterEnvMod, filterCutoffTrack;

    output stream float audioOut;

    let
    {
        midiParser = soul::midi::MPEParser;

        voices = Voice[8];
        voiceAllocator = soul::voice_allocators::Basic(8);
    }

    connection
    {
        // Forward events
        cutoffParam         -> voices.cutoffParam;
        resonanceParam      -> voices.resonanceParam;
        ampAttack           -> voices.ampAttack;
        ampDecay            -> voices.ampDecay;
        ampSustain          -> voices.ampSustain;
        ampRelease          -> voices.ampRelease;
        ampSensitivity      -> voices.ampSensitivity;
        filterAttack        -> voices.filterAttack;
        filterDecay         -> voices.filterDecay;
        filterSustain       -> voices.filterSustain;
        filterRelease       -> voices.filterRelease;
        filterSensitivity   -> voices.filterSensitivity;
        osc1waveshape       -> voices.osc1waveshape;
        osc1detuneSemitones -> voices.osc1detuneSemitones;
        osc1detune          -> voices.osc1detune;
        osc2waveshape       -> voices.osc2waveshape;
        osc2detuneSemitones -> voices.osc2detuneSemitones;
        osc2detune          -> voices.osc2detune;
        oscillatorMix       -> voices.oscillatorMix;
        filterEnvMod        -> voices.filterEnvMod;
        filterCutoffTrack   -> voices.filterCutoffTrack;

        // Pass the events to the voice allocator
        midiParser.eventOut  -> voiceAllocator.eventIn;

        // Pass the allocated events to the voice bank
        voiceAllocator.voiceEventOut -> voices.eventIn;

        // Now pass through reverb
        voices.audioOut -> audioOut;
    }
}
', 'PadSynth.soul',
        (select id from soulpatches where name = 'PadSynth'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOULPATCH', '{
    "soulPatchV1":
    {
        "ID":               "dev.soul.examples.padsynth",
        "version":          "1.0",
        "name":             "PadSynth",
        "description":      "SOUL Pad Synth example",
        "category":         "synth",
        "manufacturer":     "soul.dev",
        "isInstrument":     true,

        "source":           "PadSynth.soul"
    }
}', 'PadSynth.soulpatch',
        (select id from soulpatches where name = 'PadSynth'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'OTHER', '<?xml version="1.0" encoding="UTF-8"?>

<FACEPLATE width="8.0" height="4.0" autoSize="1" skin="Mono Modern">
  <PARAMETER bounds="1 1 2 4" parameterIDs="roomSize"/>
  <PARAMETER bounds="3 1 2 2" parameterIDs="damping"/>
  <PARAMETER bounds="3 3 2 2" parameterIDs="width"/>
  <PARAMETER bounds="5 3 2 2" parameterIDs="wetLevel"/>
  <PARAMETER bounds="7 3 2 2" parameterIDs="dryLevel"/>
  <BACKGROUND imageAlpha="1.0" colour="ff1bae5b"/>
  <LABEL bounds="5 1 4 2" text="Reverb!" stretch="0"/>
</FACEPLATE>
', 'Reverb.faceplate',
        (select id from soulpatches where name = 'Reverb'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOUL', '/*
    == SOUL example code ==

    A more complex implementation of freeverb, where events are used for sending
    parameter changes.
*/

graph Reverb  [[ main ]]
{
    input  stream float    audioIn;
    output stream float<2> audioOut;

    input
    {
        ReverbParameterProcessorParam.roomSize  [[ name: "Room Size", max: 100, init: 80, text: "tiny|small|medium|large|hall" ]];
        ReverbParameterProcessorParam.damping   [[ min: 0,  max: 100, init:  50,  name: "Damping Factor",  unit: "%",  step: 1 ]];
        ReverbParameterProcessorParam.wetLevel  [[ min: 0,  max: 100, init:  33,  name: "Wet Level",       unit: "%",  step: 1 ]];
        ReverbParameterProcessorParam.dryLevel  [[ min: 0,  max: 100, init:  40,  name: "Dry Level",       unit: "%",  step: 1 ]];
        ReverbParameterProcessorParam.width     [[ min: 0,  max: 100, init: 100,  name: "Width",           unit: "%",  step: 1 ]];
    }

    let
    {
        dryGainParameterRamp  = ParameterRamp (20.0f);
        wetGain1ParameterRamp = ParameterRamp (20.0f);
        wetGain2ParameterRamp = ParameterRamp (20.0f);
        dampingParameterRamp  = ParameterRamp (20.0f);
        feedbackParameterRamp = ParameterRamp (20.0f);

        reverbChannelLeft  = ReverbChannel (0);
        reverbChannelRight = ReverbChannel (23);
    }

    connection
    {
        // Parameter outputs to smoothing processors
        ReverbParameterProcessorParam.dryGainOut  -> dryGainParameterRamp.updateParameter;
        ReverbParameterProcessorParam.wetGain1Out -> wetGain1ParameterRamp.updateParameter;
        ReverbParameterProcessorParam.wetGain2Out -> wetGain2ParameterRamp.updateParameter;
        ReverbParameterProcessorParam.dampingOut  -> dampingParameterRamp.updateParameter;
        ReverbParameterProcessorParam.feedbackOut -> feedbackParameterRamp.updateParameter;

        // Sum the audio
        audioIn -> Mixer.audioInDry;

        // Left channel
        audioIn                            -> reverbChannelLeft.audioIn;
        dampingParameterRamp.parameterOut  -> reverbChannelLeft.damping;
        feedbackParameterRamp.parameterOut -> reverbChannelLeft.feedback;
        reverbChannelLeft.audioOut         -> Mixer.audioInLeftWet;

        // Right channel
        audioIn                            -> reverbChannelRight.audioIn;
        dampingParameterRamp.parameterOut  -> reverbChannelRight.damping;
        feedbackParameterRamp.parameterOut -> reverbChannelRight.feedback;
        reverbChannelRight.audioOut        -> Mixer.audioInRightWet;

        // Mix parameters to the mixer
        dryGainParameterRamp.parameterOut  -> Mixer.dryIn;
        wetGain1ParameterRamp.parameterOut -> Mixer.wet1In;
        wetGain2ParameterRamp.parameterOut -> Mixer.wet2In;

        // Write the mixed values to the output
        Mixer.audioOut -> audioOut;
    }
}

//==============================================================================
processor AllpassFilter (int bufferSize)
{
    output stream float audioOut;
    input  stream float audioIn;

    float[bufferSize] buffer;

    void run()
    {
        wrap<bufferSize> bufferIndex = 0;

        loop
        {
            let in = audioIn;
            let bufferedValue = buffer[bufferIndex];

            buffer[bufferIndex] = in + (bufferedValue * 0.5f);

            bufferIndex++;
            audioOut << bufferedValue - in;

            advance();
        }
    }
}

//==============================================================================
processor CombFilter (int bufferSize)
{
    output stream float audioOut;
    input  stream float audioIn, dampingIn, feedbackLevelIn;

    float[bufferSize] buffer;

    void run()
    {
        wrap<bufferSize> bufferIndex = 0;

        let gain = 0.015f;
        float last = 0.0f;

        loop
        {
            let out = buffer[bufferIndex];
            audioOut << out;

            last = (out * (1.0f - dampingIn)) + (last * dampingIn);

            buffer[bufferIndex] = (gain * audioIn) + (last * feedbackLevelIn);
            ++bufferIndex;

            advance();
        }
    }
}

//==============================================================================
processor Mixer
{
    input stream float audioInDry;
    input stream float dryIn, wet1In, wet2In,
                       audioInLeftWet, audioInRightWet;

    output stream float<2> audioOut;

    void run()
    {
        loop
        {
            let left  = (audioInLeftWet  * wet1In) + (audioInRightWet * wet2In);
            let right = (audioInRightWet * wet1In) + (audioInLeftWet  * wet2In);

            audioOut << float<2> (left, right) + audioInDry * dryIn;
            advance();
        }
    }
}


//==============================================================================
// Converts an input value into a stream (limited to the given slewRate)
processor ParameterRamp (float slewRate)
{
    input event float updateParameter;
    output stream float parameterOut;

    event updateParameter (float newTarget)
    {
        targetValue = newTarget;

        let diff = targetValue - currentValue;
        let rampSeconds = abs (diff) / slewRate;

        rampSamples   = int (processor.frequency * rampSeconds);
        rampIncrement = diff / float (rampSamples);
    }

    float targetValue;
    float currentValue;
    float rampIncrement;
    int rampSamples;

    void run()
    {
        loop
        {
            if (rampSamples > 0)
            {
                currentValue += rampIncrement;
                --rampSamples;
            }

            parameterOut << currentValue;
            advance();
        }
    }
}

//==============================================================================
// Correctly applies parameter changes to the streams of input to the algorithm
processor ReverbParameterProcessorParam
{
    input event float roomSize,
                      damping,
                      wetLevel,
                      dryLevel,
                      width;

    output event float dryGainOut,
                       wetGain1Out,
                       wetGain2Out,
                       dampingOut,
                       feedbackOut;

    event roomSize (float newValue)    { roomSizeScaled = newValue / 100.0f; onUpdate(); }
    event damping  (float newValue)    { dampingScaled  = newValue / 100.0f; onUpdate(); }
    event wetLevel (float newValue)    { wetLevelScaled = newValue / 100.0f; onUpdate(); }
    event dryLevel (float newValue)    { dryLevelScaled = newValue / 100.0f; onUpdate(); }
    event width    (float newValue)    { widthScaled    = newValue / 100.0f; onUpdate(); }

    float roomSizeScaled = 0.5f;
    float dampingScaled  = 0.5f;
    float wetLevelScaled = 0.33f;
    float dryLevelScaled = 0.4f;
    float widthScaled    = 1.0f;

    void onUpdate()
    {
        // Various tuning factors for the reverb
        let wetScaleFactor  = 3.0f;
        let dryScaleFactor  = 2.0f;

        let roomScaleFactor = 0.28f;
        let roomOffset      = 0.7f;
        let dampScaleFactor = 0.4f;

        // Write updated values
        dryGainOut  << dryLevelScaled * dryScaleFactor;
        wetGain1Out << 0.5f * wetLevelScaled * wetScaleFactor * (1.0f + widthScaled);
        wetGain2Out << 0.5f * wetLevelScaled * wetScaleFactor * (1.0f - widthScaled);
        dampingOut  << dampingScaled * dampScaleFactor;
        feedbackOut << roomSizeScaled * roomScaleFactor + roomOffset;
    }
}

//==============================================================================
// Mono freeverb implementation
graph ReverbChannel (int offset)
{
    input stream float audioIn, damping, feedback;
    output stream float audioOut;

    let
    {
        allpass_1 = AllpassFilter(225 + offset);
        allpass_2 = AllpassFilter(341 + offset);
        allpass_3 = AllpassFilter(441 + offset);
        allpass_4 = AllpassFilter(556 + offset);

        comb_1 = CombFilter(1116 + offset);
        comb_2 = CombFilter(1188 + offset);
        comb_3 = CombFilter(1277 + offset);
        comb_4 = CombFilter(1356 + offset);
        comb_5 = CombFilter(1422 + offset);
        comb_6 = CombFilter(1491 + offset);
        comb_7 = CombFilter(1557 + offset);
        comb_8 = CombFilter(1617 + offset);
    }

    connection
    {
        damping -> comb_1.dampingIn,
                   comb_2.dampingIn,
                   comb_3.dampingIn,
                   comb_4.dampingIn,
                   comb_5.dampingIn,
                   comb_6.dampingIn,
                   comb_7.dampingIn,
                   comb_8.dampingIn;

        feedback -> comb_1.feedbackLevelIn,
                    comb_2.feedbackLevelIn,
                    comb_3.feedbackLevelIn,
                    comb_4.feedbackLevelIn,
                    comb_5.feedbackLevelIn,
                    comb_6.feedbackLevelIn,
                    comb_7.feedbackLevelIn,
                    comb_8.feedbackLevelIn;

        audioIn -> comb_1.audioIn,
                   comb_2.audioIn,
                   comb_3.audioIn,
                   comb_4.audioIn,
                   comb_5.audioIn,
                   comb_6.audioIn,
                   comb_7.audioIn,
                   comb_8.audioIn;

        comb_1.audioOut,
        comb_2.audioOut,
        comb_3.audioOut,
        comb_4.audioOut,
        comb_5.audioOut,
        comb_6.audioOut,
        comb_7.audioOut,
        comb_8.audioOut  -> allpass_1.audioIn;

        allpass_1.audioOut -> allpass_2.audioIn;
        allpass_2.audioOut -> allpass_3.audioIn;
        allpass_3.audioOut -> allpass_4.audioIn;
        allpass_4.audioOut -> audioOut;
    }
}
', 'Reverb.soul',
        (select id from soulpatches where name = 'Reverb'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOULPATCH', '{
    "soulPatchV1":
    {
        "ID":               "dev.soul.examples.reverb",
        "version":          "1.0",
        "name":             "Reverb",
        "description":      "SOUL Reverb",
        "category":         "fx",
        "manufacturer":     "soul.dev",
        "isInstrument":     false,

        "source":           "Reverb.soul",
        "view":             "Reverb.faceplate"
    }
}', 'Reverb.soulpatch',
        (select id from soulpatches where name = 'Reverb'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOUL', '/*
    == SOUL example code ==

    A simple sine-wave synthesiser featuring pitch-bend support, using a basic
    envelope and voice-allocator.
*/

graph SineSynth  [[ main ]]
{
    input smoothedGain.volume [[ label: "Volume", min: -40, max: 0, init: -6, step: 1 ]];
    input event soul::midi::Message midiIn;
    output stream float audioOut;

    let
    {
        midiParser = soul::midi::MPEParser;

        voices = Voice[8];
        voiceAllocator = soul::voice_allocators::Basic(8);

        smoothedGain = soul::gain::SmoothedGainParameter (0.5f);
        gainProcessor = soul::gain::DynamicGain (float);
    }

    connection
    {
        midiIn -> midiParser.parseMIDI;

        midiParser.eventOut -> voiceAllocator.eventIn;

        // Plumb the voice allocator to the voices array
        voiceAllocator.voiceEventOut -> voices.noteOn,
                                        voices.noteOff,
                                        voices.pitchBend;

        // Sum the voices audio out to the output
        voices.audioOut -> gainProcessor.in;
        smoothedGain.gain -> gainProcessor.gain;
        gainProcessor.out -> audioOut;
    }
}

//==============================================================================
processor SineOsc
{
    input event
    {
        soul::note_events::NoteOn noteOn;
        soul::note_events::NoteOff noteOff;
        soul::note_events::PitchBend pitchBend;
    }

    output stream float audioOut;

    event noteOn (soul::note_events::NoteOn e)
    {
        notePitch = e.note;
        bendSemitones = 0.0f;
        calculatePhaseIncrement();
    }

    event noteOff (soul::note_events::NoteOff e) {}

    event pitchBend (soul::note_events::PitchBend e)
    {
        bendSemitones = e.bendSemitones;
        calculatePhaseIncrement();
    }

    float notePitch, bendSemitones, phase, phaseIncrement;

    void calculatePhaseIncrement()
    {
        let noteFrequency  = soul::noteNumberToFrequency (notePitch + bendSemitones);
        phaseIncrement = float (noteFrequency * twoPi * processor.period);
    }

    void run()
    {
        loop
        {
            phase = addModulo2Pi (phase, phaseIncrement);
            audioOut << sin (phase);
            advance();
        }
    }
}

//==============================================================================
graph Voice
{
    input event
    {
        soul::note_events::NoteOn noteOn;
        soul::note_events::NoteOff noteOff;
        soul::note_events::PitchBend pitchBend;
    }

    output stream float audioOut;

    let
    {
        osc = SineOsc;
        amplitudeEnvelope = soul::envelope::FixedAttackReleaseEnvelope (0.2f, 0.02f, 0.1f);
        attenuator = soul::gain::DynamicGain (float);
    }

    connection
    {
        noteOn          -> osc.noteOn;
        noteOff         -> osc.noteOff;
        pitchBend       -> osc.pitchBend;
        noteOn, noteOff -> amplitudeEnvelope.noteIn;

        osc.audioOut                -> attenuator.in;
        amplitudeEnvelope.levelOut  -> attenuator.gain;
        attenuator.out              -> audioOut;
    }
}
', 'SineSynth.soul',
        (select id from soulpatches where name = 'SineSynth'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOULPATCH', '{
    "soulPatchV1":
    {
        "ID":               "dev.soul.examples.sinesynth",
        "version":          "1.0",
        "name":             "SineSynth",
        "description":      "SOUL Sine Synth example",
        "category":         "synth",
        "manufacturer":     "soul.dev",
        "isInstrument":     true,

        "source":           "SineSynth.soul"
    }
}', 'SineSynth.soulpatch',
        (select id from soulpatches where name = 'SineSynth'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOUL', '/*
    == SOUL example code ==

    A handy little classic drum machine.

    This uses a single sample containing the whole kit, and then plays
    subsections of it for each drum hit. We could equally have chopped it
    up into separate wave files and loaded each one separately.

    In memory of Philip Meehan, who created these drum samples for use in
    the early Tracktion demo songs, back in around 2001.
*/

graph SOUL909  [[ main ]]
{
    input smoothedGain.volume        volume   [[ label: "Volume", min: -40, max: 0, init: -6, step: 1 ]];
    input event soul::midi::Message  midiIn;
    output stream float<2>           audioOut;

    let
    {
        midiParser = soul::midi::MPEParser;

        voices          = DrumHitPlayer[8];
        voiceAllocator  = soul::voice_allocators::Basic(8);
        smoothedGain    = soul::gain::SmoothedGainParameter (0.5f);
        gainProcessor   = soul::gain::DynamicGain (float<2>);
    }

    connection
    {
        midiIn                       -> midiParser.parseMIDI;
        midiParser.eventOut          -> voiceAllocator.eventIn;

        voiceAllocator.voiceEventOut -> voices.eventIn;

        smoothedGain.gain            -> gainProcessor.gain;
        voices.audioOut              -> gainProcessor.in;
        gainProcessor.out            -> audioOut;
    }
}

//==============================================================================
processor DrumHitPlayer
{
    input event (soul::note_events::NoteOn,
                 soul::note_events::NoteOff) eventIn;

    output stream float<2> audioOut;

    struct DrumHit
    {
        int startNote, endNote;
        float64 startTimeSeconds, lengthSeconds;
        float gaindB, pan;
    }

    external soul::audio_samples::Mono drumkit;
    external DrumHit[] drumhits;

    float64 playbackPosition, positionIncrement, playbackEndPosition;
    float<2> gain;

    event eventIn (soul::note_events::NoteOn e)
    {
        let targetNote = int (e.note);

        for (int i  = 0; i < drumhits.size; ++i)
        {
            let hit = drumhits.at(i);

            if (targetNote >= hit.startNote && targetNote <= hit.endNote)
            {
                let sampleRate = drumkit.sampleRate;
                playbackPosition = sampleRate * hit.startTimeSeconds;
                playbackEndPosition = min (playbackPosition + sampleRate * hit.lengthSeconds, drumkit.frames.size);
                positionIncrement = sampleRate / processor.frequency;
                gain = e.velocity * soul::dBtoGain (hit.gaindB) * soul::pan_law::centre3dB (hit.pan);
                break;
            }
        }
    }

    event eventIn (soul::note_events::NoteOff e) {}

    void run()
    {
        loop
        {
            if (positionIncrement > 0)
            {
                audioOut << gain * drumkit.frames.readLinearInterpolated (playbackPosition);

                playbackPosition += positionIncrement;

                if (playbackPosition >= playbackEndPosition)
                    positionIncrement = 0;
            }

            advance();
        }
    }
}
', 'SOUL909.soul',
        (select id from soulpatches where name = 'SOUL909'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOULPATCH', '{
    "soulPatchV1":
    {
        "ID":               "dev.soul.examples.SOUL909",
        "version":          "1.0",
        "name":             "SOUL-909",
        "description":      "A simple classic drumkit",
        "category":         "synth",
        "manufacturer":     "soul.dev",
        "isInstrument":     true,

        "source":           "SOUL909.soul",

        "externals":
        {
            "DrumHitPlayer::drumkit": "drumkit.ogg",

            "DrumHitPlayer::drumhits": [
                { "startNote": 36,  "endNote": 36,  "gaindB":   0.0,  "pan":   0.0,  "startTimeSeconds": 0.63443,  "lengthSeconds": 1.06964 },
                { "startNote": 38,  "endNote": 38,  "gaindB":   0.0,  "pan":   0.0,  "startTimeSeconds": 1.75525,  "lengthSeconds": 0.30451 },
                { "startNote": 40,  "endNote": 40,  "gaindB":   0.0,  "pan":   0.0,  "startTimeSeconds": 2.08037,  "lengthSeconds": 0.48710 },
                { "startNote": 41,  "endNote": 41,  "gaindB":   0.0,  "pan":   0.0,  "startTimeSeconds": 2.58825,  "lengthSeconds": 0.18646 },
                { "startNote": 43,  "endNote": 43,  "gaindB":   0.0,  "pan":   0.0,  "startTimeSeconds": 2.78997,  "lengthSeconds": 0.12428 },
                { "startNote": 45,  "endNote": 45,  "gaindB":  2.98,  "pan":   0.0,  "startTimeSeconds": 2.92620,  "lengthSeconds": 0.10615 },
                { "startNote": 48,  "endNote": 48,  "gaindB":   0.0,  "pan":   0.0,  "startTimeSeconds": 0.00000,  "lengthSeconds": 0.30443 },
                { "startNote": 49,  "endNote": 49,  "gaindB":   0.0,  "pan":   0.0,  "startTimeSeconds": 3.03674,  "lengthSeconds": 0.04283 },
                { "startNote": 50,  "endNote": 50,  "gaindB":   0.0,  "pan":   0.0,  "startTimeSeconds": 0.32749,  "lengthSeconds": 0.28518 },
                { "startNote": 51,  "endNote": 51,  "gaindB":   0.0,  "pan":   0.0,  "startTimeSeconds": 3.08158,  "lengthSeconds": 0.45000 },
                { "startNote": 54,  "endNote": 54,  "gaindB": -2.97,  "pan": -0.58,  "startTimeSeconds": 3.53794,  "lengthSeconds": 0.12083 },
                { "startNote": 57,  "endNote": 57,  "gaindB": -3.01,  "pan": -0.34,  "startTimeSeconds": 4.93584,  "lengthSeconds": 0.34850 },
                { "startNote": 58,  "endNote": 58,  "gaindB": -2.99,  "pan": -0.62,  "startTimeSeconds": 3.66152,  "lengthSeconds": 0.40353 },
                { "startNote": 59,  "endNote": 59,  "gaindB": -1.46,  "pan": -0.08,  "startTimeSeconds": 4.55833,  "lengthSeconds": 0.36687 },
                { "startNote": 60,  "endNote": 60,  "gaindB":   0.0,  "pan":  0.19,  "startTimeSeconds": 4.31731,  "lengthSeconds": 0.23693 },
                { "startNote": 61,  "endNote": 61,  "gaindB":  -3.0,  "pan":  0.40,  "startTimeSeconds": 5.85193,  "lengthSeconds": 0.89500 },
                { "startNote": 62,  "endNote": 62,  "gaindB":   0.0,  "pan":  0.63,  "startTimeSeconds": 4.07084,  "lengthSeconds": 0.23869 },
                { "startNote": 64,  "endNote": 64,  "gaindB": -2.97,  "pan":  0.38,  "startTimeSeconds": 6.76535,  "lengthSeconds": 0.79232 },
                { "startNote": 67,  "endNote": 67,  "gaindB": -2.95,  "pan": -0.31,  "startTimeSeconds": 5.30207,  "lengthSeconds": 0.53749 }
            ]
        }
    }
}', 'SOUL909.soulpatch',
        (select id from soulpatches where name = 'SOUL909'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOUL', '/*
    == SOUL example code ==
    == Implemenetation by Cesare Ferrari ==

    This is a fairly complete implementation of a TX81Z, the limitations are:

    1) No LFO
    2) Sensitivity - missing PMod, AMS and EBS
    3) No detune - you can emulate by adding a fixed component to an oscillator
    4) Envelopes - no shift
    5) Scaling - no level scaling
*/

//==============================================================================
graph ElecBass  [[ main ]]
{
    input midiParser.parseMIDI midiIn;
    output stream float audioOut;

    let
    {
        voices         = ElecBass1Voice[8];
        midiParser     = soul::midi::MPEParser;
        filter         = TX81Z::OutputFilter;
        voiceAllocator = TX81Z::PolyVoiceAllocator (-12, 8);
    }

    connection
    {
        midiParser.eventOut     -> voiceAllocator.eventIn;
        voiceAllocator.eventOut -> voices.eventIn;
        voices.audioOut         -> filter.audioIn;
        filter.audioOut         -> audioOut;
    }
}

//==============================================================================
graph ElecBass1Voice
{
    input event (soul::note_events::NoteOn,
                 soul::note_events::NoteOff,
                 soul::note_events::PitchBend) eventIn;

    output stream float audioOut;

    let
    {
        // Oscillator Parameters -  Waveshape, Volume, Freq(fixed), Freq (multiplier), Feedback
        osc1   = TX81Z::Oscillator (        1,     99,        0.0f,              1.0f,        0);
        osc2   = TX81Z::Oscillator (        1,     67,        0.0f,              0.5f,        0);
        osc3   = TX81Z::Oscillator (        1,     70,        0.0f,              9.0f,        0);
        osc4   = TX81Z::Oscillator (        1,     61,        0.0f,              3.0f,        7);

        // Envelope Parameters -                A, D1R, D1L, D2R,  R, KVS, Rate Scaling
        amplitudeEnvelope1  = TX81Z::Envelope (31,   9,   9,   0,  9,   1,            0);
        amplitudeEnvelope2  = TX81Z::Envelope (31,   1,   0,   0,  8,   0,            2);
        amplitudeEnvelope3  = TX81Z::Envelope (31,   9,  12,   6,  8,   7,            3);
        amplitudeEnvelope4  = TX81Z::Envelope (31,   1,   4,   0,  8,   3,            3);
    }

    connection
    {
        eventIn  -> osc1.eventIn,
                    osc2.eventIn,
                    osc3.eventIn,
                    osc4.eventIn,
                    amplitudeEnvelope1.eventIn,
                    amplitudeEnvelope2.eventIn,
                    amplitudeEnvelope3.eventIn,
                    amplitudeEnvelope4.eventIn;

        amplitudeEnvelope1.audioOut -> osc1.amplitudeIn;
        amplitudeEnvelope2.audioOut -> osc2.amplitudeIn;
        amplitudeEnvelope3.audioOut -> osc3.amplitudeIn;
        amplitudeEnvelope4.audioOut -> osc4.amplitudeIn;

        osc4.audioOut -> osc1.modulatorIn;
        osc3.audioOut -> osc2.modulatorIn;
        osc2.audioOut -> osc1.modulatorIn;
        osc1.audioOut -> audioOut;
    }
}', 'ElecBass1.soul',
        (select id from soulpatches where name = 'ElecBass1'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOULPATCH', '{
    "soulPatchV1":
    {
        "ID":               "dev.soul.examples.tx.elecbass1",
        "version":          "1.0",
        "name":             "ElecBass1",
        "description":      "SOUL Recreation of the classic TX electric bass preset",
        "category":         "synth",
        "manufacturer":     "soul.dev",
        "isInstrument":     true,

        "source":           [ "ElecBass1.soul", "TX81Z.soul" ]
    }
}', 'ElecBass1.soulpatch',
        (select id from soulpatches where name = 'ElecBass1'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOUL', '/*
    == SOUL example code ==
    == Implemenetation by Cesare Ferrari ==

    This is a fairly complete implementation of a TX81Z, the limitations are:

    1) No LFO
    2) Sensitivity - missing PMod, AMS and EBS
    3) No detune - you can emulate by adding a fixed component to an oscillator
    4) Envelopes - no shift
    5) Scaling - no level scaling
*/

//==============================================================================
//==============================================================================
/// Various helper functions to map instrument parameters to things we can use in our instrument
namespace TX81Z
{
    // Tuning for how much operator modulation occurs
    let operatorFactor = 4.0f;

    const float[] feedbackValues        = (0.0f, 0.008f, 0.015f, 0.024f, 0.07f, 0.12f, 0.19f, 0.26f);

    const float[] keyVelocityMapFactor1 = (0,  -34.2f,  -59.6f, -110.5f, -145.5f, -184.7f, -147.4f,  -98.8f);
    const float[] keyVelocityMapFactor2 = (0,   83.9f,  146.5f,  266.3f,  351.8f,  447.4f,  366.2f,  259.8f);
    const float[] keyVelocityMapFactor3 = (0,  -76.2f, -135.9f, -236.0f, -313.0f, -399.4f, -346.2f, -274.3f);
    const float[] keyVelocityMapFactor4 = (0,   36.7f,   69.2f,  110.7f,  147.4f,  188.2f,  185.8f,  178.0f);
    const float[] keyVelocityMapFactor5 = (0,  -15.5f,  -24.8f,  -34.2f,  -43.7f,  -53.8f,  -59.7f,  -64.9f);

    float feedbackAmount (int level)
    {
        return feedbackValues.at (level);
    }

    float keyVelocityMapping (int level, float velocity)
    {
        let velocity2 = velocity * velocity;

        let dB = keyVelocityMapFactor1.at(level) * (velocity2 * velocity2)
                   + keyVelocityMapFactor2.at(level) * (velocity2 * velocity)
                   + keyVelocityMapFactor3.at(level) * velocity2
                   + keyVelocityMapFactor4.at(level) * velocity
                   + keyVelocityMapFactor5.at(level);

        return soul::dBtoGain (dB);
    }

    float levelToGain (int level)
    {
        return soul::dBtoGain (0.74f * float(level + 1) - 73.26f);
    }

    float64 envelopeDecayLevel (int level)
    {
        return (level == 0) ? 0.0f
                            : soul::dBtoGain (-3.0f * float(15 - level));
    }

    float64 enveloperAttackRate (float64 sampleRate, float rate, float adjustment)
    {
        let attackSeconds = pow (2.0, (3.5 - ((rate + adjustment) * 0.5f)));

        // Modelled as a linear attack
        return clamp (1.0 / (sampleRate * attackSeconds), 0.0, 1.0);
    }

    // At setting 11, decay time is 1 sec for -24db of attenuation
    // Reducing the rate by 2 doubles the decay time
    float64 enveloperDecayRateFactor (float64 sampleRate, float rate, float adjustment)
    {
        if (rate == 0)
            return 1.0;

        let attenuationTime =  pow (2.0, (5.5 - ((rate + adjustment) * 0.5)));
        let samplesPer24db = sampleRate * attenuationTime;

        return 1.0 / exp (log(16.0) / samplesPer24db);
    }

    // Release rate 1 = decay rate 3, with each change in release rate
    // being two steps of decay rate
    float64 envelopeReleaseRateFactor (float64 sampleRate, float rate, float adjustment)
    {
        return enveloperDecayRateFactor (sampleRate, 1.0f + rate * 2.0f, adjustment);
    }

    // Rate scaling affects the envelope rate based on the note being played
    // Modelled as linear between a lower and upper note
    float rateScalingFactor (int rateScaling, float note)
    {
        float[4] lowerAttenuationFactors = (-0.5f, -0.5f, 0.0f, 0.5f);
        float[4] upperAttenuationFactors = (1.0f, 3.0f, 7.0f, 15.0f);

        let lowerNote = 28.0f;
        let upperNote = 110.0f;
        let lowerAttenuation = lowerAttenuationFactors.at (rateScaling);
        let upperAttenuation = upperAttenuationFactors.at (rateScaling);

        if (note < lowerNote)
            return lowerAttenuation;

        if (note > upperNote)
            return upperAttenuation;

        let notePosition = ((note - lowerNote) / (upperNote - lowerNote));
        return lowerAttenuation + (upperAttenuation - lowerAttenuation) * notePosition;
    }

    float sinOfPhase  (float phase)  { return sin (phase * float(twoPi)); }
    float sinOf2Phase (float phase)  { return sin (phase * float(twoPi * 2)); }

    // 8 waveshapes defined within the instrument, all variations on the sin function
    float getWaveshape (int waveshape, float phase)
    {
        phase = fmod (phase, 1.0f);

        if (waveshape == 1)         return sinOfPhase (phase);

        if (waveshape == 2)
        {
            if (phase < 0.25f)      return sinOfPhase (phase - 0.25f) + 1.0f;
            if (phase < 0.5f)       return sinOfPhase (phase + 0.25f) + 1.0f;
            if (phase < 0.75f)      return sinOfPhase (phase - 0.25f) - 1.0f;
                                    return sinOfPhase (phase + 0.25f) - 1.0f;
        }

        if (waveshape == 3)         return phase < 0.5 ? sinOfPhase (phase) : 0.0f;

        if (waveshape == 4)
        {
            if (phase < 0.25f)      return sinOfPhase (phase - 0.25f) + 1.0f;
            if (phase < 0.5f)       return sinOfPhase (phase + 0.25f) + 1.0f;
                                    return 0.0f;
        }

        if (waveshape == 5)         return phase < 0.5 ? sinOfPhase (2.0f * phase) : 0.0f;

        if (waveshape == 6)
        {
            if (phase < 0.125f)     return sinOf2Phase (phase - 0.125f) + 1.0f;
            if (phase < 0.25f)      return sinOf2Phase (phase + 0.125f) + 1.0f;
            if (phase < 0.375f)     return sinOf2Phase (phase - 0.125f) - 1.0f;
            if (phase < 0.5f)       return sinOf2Phase (phase + 0.125f) - 1.0f;
                                    return 0.0f;
        }

        if (waveshape == 7)
        {
            if (phase < 0.25f)      return  sinOf2Phase (phase);
            if (phase < 0.5f)       return -sinOf2Phase (phase);
                                    return 0.0f;
        }

        if (waveshape == 8)
        {
            if (phase < 0.125f)     return 1.0f + sinOf2Phase (phase - 0.125f);
            if (phase < 0.25f)      return 1.0f + sinOf2Phase (phase + 0.125f);
            if (phase < 0.375f)     return 1.0f - sinOf2Phase (phase - 0.125f);
            if (phase < 0.5f)       return 1.0f - sinOf2Phase (phase + 0.125f);
                                    return 0.0f;
        }

        return 0.0f;
    }

    //==============================================================================
    processor Oscillator (int waveshape, int level, float fixedPitch, float multiplier, int feedbackFactor)
    {
        input event (soul::note_events::NoteOn,
                     soul::note_events::NoteOff,
                     soul::note_events::PitchBend) eventIn;

        input stream float amplitudeIn, modulatorIn;

        output stream float audioOut;

        event eventIn (soul::note_events::NoteOn e)
        {
            notePitch = e.note;
            bendSemitones = 0.0f;
            calculatePhaseIncrement();

            if (! noteActive)
                phase = 0.0f;

            noteActive = true;
        }

        event eventIn (soul::note_events::NoteOff e)
        {
            noteActive = false;
        }

        event eventIn (soul::note_events::PitchBend e)
        {
            bendSemitones = e.bendSemitones;
            calculatePhaseIncrement();
        }

        float bendSemitones, notePitch, phase, phaseIncrement;

        void calculatePhaseIncrement()
        {
            let noteFrequency = fixedPitch + multiplier * soul::noteNumberToFrequency (notePitch + bendSemitones);
            phaseIncrement = noteFrequency / float (processor.frequency);
        }

        bool noteActive = false;

        void run()
        {
            let gain = levelToGain (level);
            let feedback = feedbackAmount (feedbackFactor);

            calculatePhaseIncrement();

            var oscillatorValue = 0.0f;

            loop
            {
                phase = fmod (phase + phaseIncrement, 1.0f);

                oscillatorValue = amplitudeIn * getWaveshape (waveshape, phase + (operatorFactor * modulatorIn)
                                                                               + (oscillatorValue * feedback));
                audioOut << gain * oscillatorValue;

                advance();
            }
        }
    }

    //==============================================================================
    processor Envelope (float attackRate, float decay1Rate, int decay1Level, float decay2Rate,
                        float releaseRate, int keyVelocitySensitivity, int keyRateScaling)
    {
        input event (soul::note_events::NoteOn,
                     soul::note_events::NoteOff) eventIn;

        output stream float audioOut;

        bool active = false;
        float32 keyScaling;
        float64 attackFactor, decay1Target, decay1Factor, decay2Factor, releaseFactor;
        let envelopeLimit = 0.0001;

        event eventIn (soul::note_events::NoteOn e)
        {
            active = true;
            keyScaling = TX81Z::keyVelocityMapping (keyVelocitySensitivity, e.velocity);

            let rateScalingFactor = TX81Z::rateScalingFactor (keyRateScaling, e.note);

            attackFactor  = TX81Z::enveloperAttackRate (processor.frequency, attackRate, rateScalingFactor);
            decay1Target  = TX81Z::envelopeDecayLevel (decay1Level);
            decay1Factor  = TX81Z::enveloperDecayRateFactor (processor.frequency, decay1Rate, rateScalingFactor);
            decay2Factor  = TX81Z::enveloperDecayRateFactor (processor.frequency, decay2Rate, rateScalingFactor);
            releaseFactor = TX81Z::envelopeReleaseRateFactor (processor.frequency, releaseRate, rateScalingFactor);
        }

        event eventIn (soul::note_events::NoteOff e)
        {
            active = false;
        }

        void run()
        {
            var value = 0.0;

            loop
            {
                while (! active)
                {
                    audioOut << 0.0f;
                    advance();
                }

                // Attack
                while (active && value < 1.0)
                {
                    // Model as linear attack - not accurate (more like an S shape in the instrument)
                    value += attackFactor;
                    audioOut << keyScaling * float (value);
                    advance();
                }

                value = 1.0;

                // Decay1
                while (active && value > decay1Target)
                {
                    value *= decay1Factor;
                    audioOut << keyScaling * float (value);
                    advance();
                }

                // Decay2
                while (active)
                {
                    value *= decay2Factor;
                    audioOut << keyScaling * float (value);
                    advance();

                    if (value < envelopeLimit)
                        active = false;
                }

                // Release
                while (! active && value > envelopeLimit)
                {
                    value *= releaseFactor;
                    audioOut << keyScaling * float (value);
                    advance();
                }
            }
        }
    }

    //==============================================================================
    // Remove DC from the output, apply some gain reduction
    processor OutputFilter
    {
        input stream float  audioIn;
        output stream float audioOut;

        void run()
        {
            let gain = soul::dBtoGain (-6.0f);

            float previousIn, previousOut;

            loop
            {
                let y = audioIn - previousIn + 0.997f * previousOut;
                previousIn = audioIn;
                previousOut = y;

                audioOut << gain * y;
                advance();
            }
        }
    }

    //==============================================================================
    // The voice allocator supports an offset as some patches are pitched up/down
    processor PolyVoiceAllocator (int noteOffset, int voiceCount)
    {
        input event (soul::note_events::NoteOn,
                     soul::note_events::NoteOff,
                     soul::note_events::PitchBend) eventIn;


        output event (soul::note_events::NoteOn,
                      soul::note_events::NoteOff,
                      soul::note_events::PitchBend) eventOut[voiceCount];

        event eventIn (soul::note_events::NoteOn e)
        {
            e.note = clamp (e.note + noteOffset, 24.0f, 127.0f);

            wrap<voiceCount> allocatedVoice = 0;
            var allocatedVoiceAge = voiceInfo[allocatedVoice].voiceAge;

            // Find the oldest voice to reuse
            for (int i = 1; i < voiceCount; i++)
            {
                let age = voiceInfo.at(i).voiceAge;

                if (age < allocatedVoiceAge)
                {
                    allocatedVoiceAge = age;
                    allocatedVoice = wrap<voiceCount>(i);
                }
            }

            // Update the VoiceInfo for our chosen voice
            voiceInfo[allocatedVoice].channel  = e.channel;
            voiceInfo[allocatedVoice].note     = e.note;
            voiceInfo[allocatedVoice].voiceAge = nextAllocatedVoiceAge++;

            // Send the note on to the voice
            eventOut[allocatedVoice] << e;
        }

        event eventIn (soul::note_events::NoteOff e)
        {
            e.note = clamp (e.note + noteOffset, 24.0f, 127.0f);

            // Release all voices associated with this note/channel
            wrap<voiceCount> voice = 0;

            loop (voiceCount)
            {
                if (voiceInfo[voice].channel == e.channel
                    && voiceInfo[voice].note == e.note)
                {
                    // Mark the voice as being unused
                    voiceInfo[voice].voiceAge = nextUnallocatedVoiceAge++;
                    eventOut[voice] << e;
                }

                ++voice;
            }
        }

        event eventIn (soul::note_events::PitchBend e)
        {
            // Forward the pitch bend to all notes on this channel
            wrap<voiceCount> voice = 0;

            loop (voiceCount)
            {
                if (voiceInfo[voice].channel == e.channel)
                    eventOut[voice] << e;

                voice++;
            }
        }

        struct VoiceInfo
        {
            bool active;
            float note;
            int channel, voiceAge;
        }

        int nextAllocatedVoiceAge   = 1000000000;
        int nextUnallocatedVoiceAge = 1;

        VoiceInfo[voiceCount] voiceInfo;
    }
}', 'TX81Z.soul',
        (select id from soulpatches where name = 'ElecBass1'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOUL', '/*
    == SOUL example code ==
    == Implemenetation by Cesare Ferrari ==

    This is a fairly complete implementation of a TX81Z, the limitations are:

    1) No LFO
    2) Sensitivity - missing PMod, AMS and EBS
    3) No detune - you can emulate by adding a fixed component to an oscillator
    4) Envelopes - no shift
    5) Scaling - no level scaling
*/

//==============================================================================
graph ElectoPiano  [[ main ]]
{
    input midiParser.parseMIDI midiIn;
    output stream float audioOut;

    let
    {
        voices         = ElectroPiano[8];
        midiParser     = soul::midi::MPEParser;
        filter         = TX81Z::OutputFilter;
        voiceAllocator = TX81Z::PolyVoiceAllocator (-12, 8);
    }

    connection
    {
        midiParser.eventOut     -> voiceAllocator.eventIn;
        voiceAllocator.eventOut -> voices.eventIn;
        voices.audioOut         -> filter.audioIn;
        filter.audioOut         -> audioOut;
    }
}

//==============================================================================
graph ElectroPiano
{
    input event (soul::note_events::NoteOn,
                 soul::note_events::NoteOff,
                 soul::note_events::PitchBend) eventIn;

    output stream float audioOut;

    let
    {
        // Oscillator Parameters -        Waveshape, Volume, Freq(fixed), Freq (multiplier), Feedback
        osc1         = TX81Z::Oscillator (        1,     99,        0.0f,              1.0f,        0);
        osc2         = TX81Z::Oscillator (        1,     84,        1.0f,              1.0f,        0);
        osc3         = TX81Z::Oscillator (        1,     99,        1.0f,              1.0f,        0);
        osc4         = TX81Z::Oscillator (        1,     54,       -1.0f,             14.0f,        0);

        // Envelope Parameters -                A, D1R, D1L, D2R,  R, KVS, Rate Scaling
        amplitudeEnvelope1  = TX81Z::Envelope (24,   4,  13,   3,  6,   3,            2);
        amplitudeEnvelope2  = TX81Z::Envelope (31,   4,  13,   3,  4,   3,            2);
        amplitudeEnvelope3  = TX81Z::Envelope (31,  13,  12,   8,  7,   4,            1);
        amplitudeEnvelope4  = TX81Z::Envelope (31,   6,  12,   4,  7,   4,            2);
    }

    connection
    {
        eventIn  -> osc1.eventIn,
                    osc2.eventIn,
                    osc3.eventIn,
                    osc4.eventIn,
                    amplitudeEnvelope1.eventIn,
                    amplitudeEnvelope2.eventIn,
                    amplitudeEnvelope3.eventIn,
                    amplitudeEnvelope4.eventIn;

        amplitudeEnvelope1.audioOut -> osc1.amplitudeIn;
        amplitudeEnvelope2.audioOut -> osc2.amplitudeIn;
        amplitudeEnvelope3.audioOut -> osc3.amplitudeIn;
        amplitudeEnvelope4.audioOut -> osc4.amplitudeIn;

        osc4.audioOut -> osc3.modulatorIn;
        osc3.audioOut -> audioOut;
        osc2.audioOut -> osc1.modulatorIn;
        osc1.audioOut -> audioOut;
    }
}
', 'ElectroPiano.soul',
        (select id from soulpatches where name = 'ElectroPiano'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOULPATCH', '{
    "soulPatchV1":
    {
        "ID":               "dev.soul.examples.tx.electropiano",
        "version":          "1.0",
        "name":             "ElectroPiano",
        "description":      "SOUL Recreation of the classic TX electric piano preset",
        "category":         "synth",
        "manufacturer":     "soul.dev",
        "isInstrument":     true,

        "source":           [ "ElectroPiano.soul", "TX81Z.soul" ]
    }
}
', 'ElectroPiano.soulpatch',
        (select id from soulpatches where name = 'ElectroPiano'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOUL', '/*
    == SOUL example code ==
    == Implemenetation by Cesare Ferrari ==

    This is a fairly complete implementation of a TX81Z, the limitations are:

    1) No LFO
    2) Sensitivity - missing PMod, AMS and EBS
    3) No detune - you can emulate by adding a fixed component to an oscillator
    4) Envelopes - no shift
    5) Scaling - no level scaling
*/

//==============================================================================
//==============================================================================
/// Various helper functions to map instrument parameters to things we can use in our instrument
namespace TX81Z
{
    // Tuning for how much operator modulation occurs
    let operatorFactor = 4.0f;

    const float[] feedbackValues        = (0.0f, 0.008f, 0.015f, 0.024f, 0.07f, 0.12f, 0.19f, 0.26f);

    const float[] keyVelocityMapFactor1 = (0,  -34.2f,  -59.6f, -110.5f, -145.5f, -184.7f, -147.4f,  -98.8f);
    const float[] keyVelocityMapFactor2 = (0,   83.9f,  146.5f,  266.3f,  351.8f,  447.4f,  366.2f,  259.8f);
    const float[] keyVelocityMapFactor3 = (0,  -76.2f, -135.9f, -236.0f, -313.0f, -399.4f, -346.2f, -274.3f);
    const float[] keyVelocityMapFactor4 = (0,   36.7f,   69.2f,  110.7f,  147.4f,  188.2f,  185.8f,  178.0f);
    const float[] keyVelocityMapFactor5 = (0,  -15.5f,  -24.8f,  -34.2f,  -43.7f,  -53.8f,  -59.7f,  -64.9f);

    float feedbackAmount (int level)
    {
        return feedbackValues.at (level);
    }

    float keyVelocityMapping (int level, float velocity)
    {
        let velocity2 = velocity * velocity;

        let dB = keyVelocityMapFactor1.at(level) * (velocity2 * velocity2)
                   + keyVelocityMapFactor2.at(level) * (velocity2 * velocity)
                   + keyVelocityMapFactor3.at(level) * velocity2
                   + keyVelocityMapFactor4.at(level) * velocity
                   + keyVelocityMapFactor5.at(level);

        return soul::dBtoGain (dB);
    }

    float levelToGain (int level)
    {
        return soul::dBtoGain (0.74f * float(level + 1) - 73.26f);
    }

    float64 envelopeDecayLevel (int level)
    {
        return (level == 0) ? 0.0f
                            : soul::dBtoGain (-3.0f * float(15 - level));
    }

    float64 enveloperAttackRate (float64 sampleRate, float rate, float adjustment)
    {
        let attackSeconds = pow (2.0, (3.5 - ((rate + adjustment) * 0.5f)));

        // Modelled as a linear attack
        return clamp (1.0 / (sampleRate * attackSeconds), 0.0, 1.0);
    }

    // At setting 11, decay time is 1 sec for -24db of attenuation
    // Reducing the rate by 2 doubles the decay time
    float64 enveloperDecayRateFactor (float64 sampleRate, float rate, float adjustment)
    {
        if (rate == 0)
            return 1.0;

        let attenuationTime =  pow (2.0, (5.5 - ((rate + adjustment) * 0.5)));
        let samplesPer24db = sampleRate * attenuationTime;

        return 1.0 / exp (log(16.0) / samplesPer24db);
    }

    // Release rate 1 = decay rate 3, with each change in release rate
    // being two steps of decay rate
    float64 envelopeReleaseRateFactor (float64 sampleRate, float rate, float adjustment)
    {
        return enveloperDecayRateFactor (sampleRate, 1.0f + rate * 2.0f, adjustment);
    }

    // Rate scaling affects the envelope rate based on the note being played
    // Modelled as linear between a lower and upper note
    float rateScalingFactor (int rateScaling, float note)
    {
        float[4] lowerAttenuationFactors = (-0.5f, -0.5f, 0.0f, 0.5f);
        float[4] upperAttenuationFactors = (1.0f, 3.0f, 7.0f, 15.0f);

        let lowerNote = 28.0f;
        let upperNote = 110.0f;
        let lowerAttenuation = lowerAttenuationFactors.at (rateScaling);
        let upperAttenuation = upperAttenuationFactors.at (rateScaling);

        if (note < lowerNote)
            return lowerAttenuation;

        if (note > upperNote)
            return upperAttenuation;

        let notePosition = ((note - lowerNote) / (upperNote - lowerNote));
        return lowerAttenuation + (upperAttenuation - lowerAttenuation) * notePosition;
    }

    float sinOfPhase  (float phase)  { return sin (phase * float(twoPi)); }
    float sinOf2Phase (float phase)  { return sin (phase * float(twoPi * 2)); }

    // 8 waveshapes defined within the instrument, all variations on the sin function
    float getWaveshape (int waveshape, float phase)
    {
        phase = fmod (phase, 1.0f);

        if (waveshape == 1)         return sinOfPhase (phase);

        if (waveshape == 2)
        {
            if (phase < 0.25f)      return sinOfPhase (phase - 0.25f) + 1.0f;
            if (phase < 0.5f)       return sinOfPhase (phase + 0.25f) + 1.0f;
            if (phase < 0.75f)      return sinOfPhase (phase - 0.25f) - 1.0f;
                                    return sinOfPhase (phase + 0.25f) - 1.0f;
        }

        if (waveshape == 3)         return phase < 0.5 ? sinOfPhase (phase) : 0.0f;

        if (waveshape == 4)
        {
            if (phase < 0.25f)      return sinOfPhase (phase - 0.25f) + 1.0f;
            if (phase < 0.5f)       return sinOfPhase (phase + 0.25f) + 1.0f;
                                    return 0.0f;
        }

        if (waveshape == 5)         return phase < 0.5 ? sinOfPhase (2.0f * phase) : 0.0f;

        if (waveshape == 6)
        {
            if (phase < 0.125f)     return sinOf2Phase (phase - 0.125f) + 1.0f;
            if (phase < 0.25f)      return sinOf2Phase (phase + 0.125f) + 1.0f;
            if (phase < 0.375f)     return sinOf2Phase (phase - 0.125f) - 1.0f;
            if (phase < 0.5f)       return sinOf2Phase (phase + 0.125f) - 1.0f;
                                    return 0.0f;
        }

        if (waveshape == 7)
        {
            if (phase < 0.25f)      return  sinOf2Phase (phase);
            if (phase < 0.5f)       return -sinOf2Phase (phase);
                                    return 0.0f;
        }

        if (waveshape == 8)
        {
            if (phase < 0.125f)     return 1.0f + sinOf2Phase (phase - 0.125f);
            if (phase < 0.25f)      return 1.0f + sinOf2Phase (phase + 0.125f);
            if (phase < 0.375f)     return 1.0f - sinOf2Phase (phase - 0.125f);
            if (phase < 0.5f)       return 1.0f - sinOf2Phase (phase + 0.125f);
                                    return 0.0f;
        }

        return 0.0f;
    }

    //==============================================================================
    processor Oscillator (int waveshape, int level, float fixedPitch, float multiplier, int feedbackFactor)
    {
        input event (soul::note_events::NoteOn,
                     soul::note_events::NoteOff,
                     soul::note_events::PitchBend) eventIn;

        input stream float amplitudeIn, modulatorIn;

        output stream float audioOut;

        event eventIn (soul::note_events::NoteOn e)
        {
            notePitch = e.note;
            bendSemitones = 0.0f;
            calculatePhaseIncrement();

            if (! noteActive)
                phase = 0.0f;

            noteActive = true;
        }

        event eventIn (soul::note_events::NoteOff e)
        {
            noteActive = false;
        }

        event eventIn (soul::note_events::PitchBend e)
        {
            bendSemitones = e.bendSemitones;
            calculatePhaseIncrement();
        }

        float bendSemitones, notePitch, phase, phaseIncrement;

        void calculatePhaseIncrement()
        {
            let noteFrequency = fixedPitch + multiplier * soul::noteNumberToFrequency (notePitch + bendSemitones);
            phaseIncrement = noteFrequency / float (processor.frequency);
        }

        bool noteActive = false;

        void run()
        {
            let gain = levelToGain (level);
            let feedback = feedbackAmount (feedbackFactor);

            calculatePhaseIncrement();

            var oscillatorValue = 0.0f;

            loop
            {
                phase = fmod (phase + phaseIncrement, 1.0f);

                oscillatorValue = amplitudeIn * getWaveshape (waveshape, phase + (operatorFactor * modulatorIn)
                                                                               + (oscillatorValue * feedback));
                audioOut << gain * oscillatorValue;

                advance();
            }
        }
    }

    //==============================================================================
    processor Envelope (float attackRate, float decay1Rate, int decay1Level, float decay2Rate,
                        float releaseRate, int keyVelocitySensitivity, int keyRateScaling)
    {
        input event (soul::note_events::NoteOn,
                     soul::note_events::NoteOff) eventIn;

        output stream float audioOut;

        bool active = false;
        float32 keyScaling;
        float64 attackFactor, decay1Target, decay1Factor, decay2Factor, releaseFactor;
        let envelopeLimit = 0.0001;

        event eventIn (soul::note_events::NoteOn e)
        {
            active = true;
            keyScaling = TX81Z::keyVelocityMapping (keyVelocitySensitivity, e.velocity);

            let rateScalingFactor = TX81Z::rateScalingFactor (keyRateScaling, e.note);

            attackFactor  = TX81Z::enveloperAttackRate (processor.frequency, attackRate, rateScalingFactor);
            decay1Target  = TX81Z::envelopeDecayLevel (decay1Level);
            decay1Factor  = TX81Z::enveloperDecayRateFactor (processor.frequency, decay1Rate, rateScalingFactor);
            decay2Factor  = TX81Z::enveloperDecayRateFactor (processor.frequency, decay2Rate, rateScalingFactor);
            releaseFactor = TX81Z::envelopeReleaseRateFactor (processor.frequency, releaseRate, rateScalingFactor);
        }

        event eventIn (soul::note_events::NoteOff e)
        {
            active = false;
        }

        void run()
        {
            var value = 0.0;

            loop
            {
                while (! active)
                {
                    audioOut << 0.0f;
                    advance();
                }

                // Attack
                while (active && value < 1.0)
                {
                    // Model as linear attack - not accurate (more like an S shape in the instrument)
                    value += attackFactor;
                    audioOut << keyScaling * float (value);
                    advance();
                }

                value = 1.0;

                // Decay1
                while (active && value > decay1Target)
                {
                    value *= decay1Factor;
                    audioOut << keyScaling * float (value);
                    advance();
                }

                // Decay2
                while (active)
                {
                    value *= decay2Factor;
                    audioOut << keyScaling * float (value);
                    advance();

                    if (value < envelopeLimit)
                        active = false;
                }

                // Release
                while (! active && value > envelopeLimit)
                {
                    value *= releaseFactor;
                    audioOut << keyScaling * float (value);
                    advance();
                }
            }
        }
    }

    //==============================================================================
    // Remove DC from the output, apply some gain reduction
    processor OutputFilter
    {
        input stream float  audioIn;
        output stream float audioOut;

        void run()
        {
            let gain = soul::dBtoGain (-6.0f);

            float previousIn, previousOut;

            loop
            {
                let y = audioIn - previousIn + 0.997f * previousOut;
                previousIn = audioIn;
                previousOut = y;

                audioOut << gain * y;
                advance();
            }
        }
    }

    //==============================================================================
    // The voice allocator supports an offset as some patches are pitched up/down
    processor PolyVoiceAllocator (int noteOffset, int voiceCount)
    {
        input event (soul::note_events::NoteOn,
                     soul::note_events::NoteOff,
                     soul::note_events::PitchBend) eventIn;


        output event (soul::note_events::NoteOn,
                      soul::note_events::NoteOff,
                      soul::note_events::PitchBend) eventOut[voiceCount];

        event eventIn (soul::note_events::NoteOn e)
        {
            e.note = clamp (e.note + noteOffset, 24.0f, 127.0f);

            wrap<voiceCount> allocatedVoice = 0;
            var allocatedVoiceAge = voiceInfo[allocatedVoice].voiceAge;

            // Find the oldest voice to reuse
            for (int i = 1; i < voiceCount; i++)
            {
                let age = voiceInfo.at(i).voiceAge;

                if (age < allocatedVoiceAge)
                {
                    allocatedVoiceAge = age;
                    allocatedVoice = wrap<voiceCount>(i);
                }
            }

            // Update the VoiceInfo for our chosen voice
            voiceInfo[allocatedVoice].channel  = e.channel;
            voiceInfo[allocatedVoice].note     = e.note;
            voiceInfo[allocatedVoice].voiceAge = nextAllocatedVoiceAge++;

            // Send the note on to the voice
            eventOut[allocatedVoice] << e;
        }

        event eventIn (soul::note_events::NoteOff e)
        {
            e.note = clamp (e.note + noteOffset, 24.0f, 127.0f);

            // Release all voices associated with this note/channel
            wrap<voiceCount> voice = 0;

            loop (voiceCount)
            {
                if (voiceInfo[voice].channel == e.channel
                    && voiceInfo[voice].note == e.note)
                {
                    // Mark the voice as being unused
                    voiceInfo[voice].voiceAge = nextUnallocatedVoiceAge++;
                    eventOut[voice] << e;
                }

                ++voice;
            }
        }

        event eventIn (soul::note_events::PitchBend e)
        {
            // Forward the pitch bend to all notes on this channel
            wrap<voiceCount> voice = 0;

            loop (voiceCount)
            {
                if (voiceInfo[voice].channel == e.channel)
                    eventOut[voice] << e;

                voice++;
            }
        }

        struct VoiceInfo
        {
            bool active;
            float note;
            int channel, voiceAge;
        }

        int nextAllocatedVoiceAge   = 1000000000;
        int nextUnallocatedVoiceAge = 1;

        VoiceInfo[voiceCount] voiceInfo;
    }
}
', 'TX81Z.soul',
        (select id from soulpatches where name = 'ElectroPiano'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOUL', '/*
    == SOUL example code ==
    == Implemenetation by Cesare Ferrari ==

    This is a fairly complete implementation of a TX81Z, the limitations are:

    1) No LFO
    2) Sensitivity - missing PMod, AMS and EBS
    3) No detune - you can emulate by adding a fixed component to an oscillator
    4) Envelopes - no shift
    5) Scaling - no level scaling
*/

//==============================================================================
graph LatelyBass  [[ main ]]
{
    input event soul::midi::Message midiIn;
    output stream float audioOut;

    let
    {
        voices         = LatelyBassVoice[8];
        midiParser     = soul::midi::MPEParser;
        filter         = TX81Z::OutputFilter;
        voiceAllocator = TX81Z::PolyVoiceAllocator (-12, 8);
    }

    connection
    {
        midiIn                  -> midiParser.parseMIDI;
        midiParser.eventOut     -> voiceAllocator.eventIn;
        voiceAllocator.eventOut -> voices.eventIn;
        voices.audioOut         -> filter.audioIn;
        filter.audioOut         -> audioOut;
    }
}

//==============================================================================
graph LatelyBassVoice
{
    input event (soul::note_events::NoteOn,
                 soul::note_events::NoteOff,
                 soul::note_events::PitchBend) eventIn;

    output stream float audioOut;

    let
    {
        // Oscillator Parameters -        Waveshape, Volume, Freq(fixed), Freq (multiplier), Feedback
        osc1         = TX81Z::Oscillator (        1,     99,        0.0f,              0.5f,        0);
        osc2         = TX81Z::Oscillator (        1,     74,        0.0f,              0.5f,        0);
        osc3         = TX81Z::Oscillator (        5,     71,        0.0f,              1.0f,        0);
        osc4         = TX81Z::Oscillator (        1,     79,        0.0f,              1.0f,        7);

        // Envelope Parameters -                A, D1R, D1L, D2R,  R, KVS, Rate Scaling
        amplitudeEnvelope1  = TX81Z::Envelope (31,   9,   0,   0,  8,   0,            1);
        amplitudeEnvelope2  = TX81Z::Envelope (31,   9,   0,   0,  8,   1,            1);
        amplitudeEnvelope3  = TX81Z::Envelope (31,  17,   0,   0,  8,   1,            1);
        amplitudeEnvelope4  = TX81Z::Envelope (31,  16,   3,   7,  8,   3,            0);
    }

    connection
    {
        eventIn  -> osc1.eventIn,
                    osc2.eventIn,
                    osc3.eventIn,
                    osc4.eventIn,
                    amplitudeEnvelope1.eventIn,
                    amplitudeEnvelope2.eventIn,
                    amplitudeEnvelope3.eventIn,
                    amplitudeEnvelope4.eventIn;

        amplitudeEnvelope1.audioOut -> osc1.amplitudeIn;
        amplitudeEnvelope2.audioOut -> osc2.amplitudeIn;
        amplitudeEnvelope3.audioOut -> osc3.amplitudeIn;
        amplitudeEnvelope4.audioOut -> osc4.amplitudeIn;

        osc4.audioOut -> osc2.modulatorIn;
        osc3.audioOut -> osc2.modulatorIn;
        osc2.audioOut -> osc1.modulatorIn;
        osc1.audioOut -> audioOut;
    }
}
', 'LatelyBass.soul',
        (select id from soulpatches where name = 'LatelyBass'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOULPATCH', '{
    "soulPatchV1":
    {
        "ID":               "dev.soul.examples.tx.latelybass",
        "version":          "1.0",
        "name":             "LatelyBass",
        "description":      "SOUL Recreation of the classic TX Lately Bass preset",
        "category":         "synth",
        "manufacturer":     "soul.dev",
        "isInstrument":     true,

        "source":           [ "LatelyBass.soul", "TX81Z.soul" ]
    }
}', 'LatelyBass.soulpatch',
        (select id from soulpatches where name = 'LatelyBass'));
insert into spfiles (id, created_at, updated_at, filetype, content, name, soulpatch_id)
VALUES (nextval('hibernate_sequence'), current_timestamp, current_timestamp, 'SOUL', '/*
    == SOUL example code ==
    == Implemenetation by Cesare Ferrari ==

    This is a fairly complete implementation of a TX81Z, the limitations are:

    1) No LFO
    2) Sensitivity - missing PMod, AMS and EBS
    3) No detune - you can emulate by adding a fixed component to an oscillator
    4) Envelopes - no shift
    5) Scaling - no level scaling
*/

//==============================================================================
//==============================================================================
/// Various helper functions to map instrument parameters to things we can use in our instrument
namespace TX81Z
{
    // Tuning for how much operator modulation occurs
    let operatorFactor = 4.0f;

    const float[] feedbackValues        = (0.0f, 0.008f, 0.015f, 0.024f, 0.07f, 0.12f, 0.19f, 0.26f);

    const float[] keyVelocityMapFactor1 = (0,  -34.2f,  -59.6f, -110.5f, -145.5f, -184.7f, -147.4f,  -98.8f);
    const float[] keyVelocityMapFactor2 = (0,   83.9f,  146.5f,  266.3f,  351.8f,  447.4f,  366.2f,  259.8f);
    const float[] keyVelocityMapFactor3 = (0,  -76.2f, -135.9f, -236.0f, -313.0f, -399.4f, -346.2f, -274.3f);
    const float[] keyVelocityMapFactor4 = (0,   36.7f,   69.2f,  110.7f,  147.4f,  188.2f,  185.8f,  178.0f);
    const float[] keyVelocityMapFactor5 = (0,  -15.5f,  -24.8f,  -34.2f,  -43.7f,  -53.8f,  -59.7f,  -64.9f);

    float feedbackAmount (int level)
    {
        return feedbackValues.at (level);
    }

    float keyVelocityMapping (int level, float velocity)
    {
        let velocity2 = velocity * velocity;

        let dB = keyVelocityMapFactor1.at(level) * (velocity2 * velocity2)
                   + keyVelocityMapFactor2.at(level) * (velocity2 * velocity)
                   + keyVelocityMapFactor3.at(level) * velocity2
                   + keyVelocityMapFactor4.at(level) * velocity
                   + keyVelocityMapFactor5.at(level);

        return soul::dBtoGain (dB);
    }

    float levelToGain (int level)
    {
        return soul::dBtoGain (0.74f * float(level + 1) - 73.26f);
    }

    float64 envelopeDecayLevel (int level)
    {
        return (level == 0) ? 0.0f
                            : soul::dBtoGain (-3.0f * float(15 - level));
    }

    float64 enveloperAttackRate (float64 sampleRate, float rate, float adjustment)
    {
        let attackSeconds = pow (2.0, (3.5 - ((rate + adjustment) * 0.5f)));

        // Modelled as a linear attack
        return clamp (1.0 / (sampleRate * attackSeconds), 0.0, 1.0);
    }

    // At setting 11, decay time is 1 sec for -24db of attenuation
    // Reducing the rate by 2 doubles the decay time
    float64 enveloperDecayRateFactor (float64 sampleRate, float rate, float adjustment)
    {
        if (rate == 0)
            return 1.0;

        let attenuationTime =  pow (2.0, (5.5 - ((rate + adjustment) * 0.5)));
        let samplesPer24db = sampleRate * attenuationTime;

        return 1.0 / exp (log(16.0) / samplesPer24db);
    }

    // Release rate 1 = decay rate 3, with each change in release rate
    // being two steps of decay rate
    float64 envelopeReleaseRateFactor (float64 sampleRate, float rate, float adjustment)
    {
        return enveloperDecayRateFactor (sampleRate, 1.0f + rate * 2.0f, adjustment);
    }

    // Rate scaling affects the envelope rate based on the note being played
    // Modelled as linear between a lower and upper note
    float rateScalingFactor (int rateScaling, float note)
    {
        float[4] lowerAttenuationFactors = (-0.5f, -0.5f, 0.0f, 0.5f);
        float[4] upperAttenuationFactors = (1.0f, 3.0f, 7.0f, 15.0f);

        let lowerNote = 28.0f;
        let upperNote = 110.0f;
        let lowerAttenuation = lowerAttenuationFactors.at (rateScaling);
        let upperAttenuation = upperAttenuationFactors.at (rateScaling);

        if (note < lowerNote)
            return lowerAttenuation;

        if (note > upperNote)
            return upperAttenuation;

        let notePosition = ((note - lowerNote) / (upperNote - lowerNote));
        return lowerAttenuation + (upperAttenuation - lowerAttenuation) * notePosition;
    }

    float sinOfPhase  (float phase)  { return sin (phase * float(twoPi)); }
    float sinOf2Phase (float phase)  { return sin (phase * float(twoPi * 2)); }

    // 8 waveshapes defined within the instrument, all variations on the sin function
    float getWaveshape (int waveshape, float phase)
    {
        phase = fmod (phase, 1.0f);

        if (waveshape == 1)         return sinOfPhase (phase);

        if (waveshape == 2)
        {
            if (phase < 0.25f)      return sinOfPhase (phase - 0.25f) + 1.0f;
            if (phase < 0.5f)       return sinOfPhase (phase + 0.25f) + 1.0f;
            if (phase < 0.75f)      return sinOfPhase (phase - 0.25f) - 1.0f;
                                    return sinOfPhase (phase + 0.25f) - 1.0f;
        }

        if (waveshape == 3)         return phase < 0.5 ? sinOfPhase (phase) : 0.0f;

        if (waveshape == 4)
        {
            if (phase < 0.25f)      return sinOfPhase (phase - 0.25f) + 1.0f;
            if (phase < 0.5f)       return sinOfPhase (phase + 0.25f) + 1.0f;
                                    return 0.0f;
        }

        if (waveshape == 5)         return phase < 0.5 ? sinOfPhase (2.0f * phase) : 0.0f;

        if (waveshape == 6)
        {
            if (phase < 0.125f)     return sinOf2Phase (phase - 0.125f) + 1.0f;
            if (phase < 0.25f)      return sinOf2Phase (phase + 0.125f) + 1.0f;
            if (phase < 0.375f)     return sinOf2Phase (phase - 0.125f) - 1.0f;
            if (phase < 0.5f)       return sinOf2Phase (phase + 0.125f) - 1.0f;
                                    return 0.0f;
        }

        if (waveshape == 7)
        {
            if (phase < 0.25f)      return  sinOf2Phase (phase);
            if (phase < 0.5f)       return -sinOf2Phase (phase);
                                    return 0.0f;
        }

        if (waveshape == 8)
        {
            if (phase < 0.125f)     return 1.0f + sinOf2Phase (phase - 0.125f);
            if (phase < 0.25f)      return 1.0f + sinOf2Phase (phase + 0.125f);
            if (phase < 0.375f)     return 1.0f - sinOf2Phase (phase - 0.125f);
            if (phase < 0.5f)       return 1.0f - sinOf2Phase (phase + 0.125f);
                                    return 0.0f;
        }

        return 0.0f;
    }

    //==============================================================================
    processor Oscillator (int waveshape, int level, float fixedPitch, float multiplier, int feedbackFactor)
    {
        input event (soul::note_events::NoteOn,
                     soul::note_events::NoteOff,
                     soul::note_events::PitchBend) eventIn;

        input stream float amplitudeIn, modulatorIn;

        output stream float audioOut;

        event eventIn (soul::note_events::NoteOn e)
        {
            notePitch = e.note;
            bendSemitones = 0.0f;
            calculatePhaseIncrement();

            if (! noteActive)
                phase = 0.0f;

            noteActive = true;
        }

        event eventIn (soul::note_events::NoteOff e)
        {
            noteActive = false;
        }

        event eventIn (soul::note_events::PitchBend e)
        {
            bendSemitones = e.bendSemitones;
            calculatePhaseIncrement();
        }

        float bendSemitones, notePitch, phase, phaseIncrement;

        void calculatePhaseIncrement()
        {
            let noteFrequency = fixedPitch + multiplier * soul::noteNumberToFrequency (notePitch + bendSemitones);
            phaseIncrement = noteFrequency / float (processor.frequency);
        }

        bool noteActive = false;

        void run()
        {
            let gain = levelToGain (level);
            let feedback = feedbackAmount (feedbackFactor);

            calculatePhaseIncrement();

            var oscillatorValue = 0.0f;

            loop
            {
                phase = fmod (phase + phaseIncrement, 1.0f);

                oscillatorValue = amplitudeIn * getWaveshape (waveshape, phase + (operatorFactor * modulatorIn)
                                                                               + (oscillatorValue * feedback));
                audioOut << gain * oscillatorValue;

                advance();
            }
        }
    }

    //==============================================================================
    processor Envelope (float attackRate, float decay1Rate, int decay1Level, float decay2Rate,
                        float releaseRate, int keyVelocitySensitivity, int keyRateScaling)
    {
        input event (soul::note_events::NoteOn,
                     soul::note_events::NoteOff) eventIn;

        output stream float audioOut;

        bool active = false;
        float32 keyScaling;
        float64 attackFactor, decay1Target, decay1Factor, decay2Factor, releaseFactor;
        let envelopeLimit = 0.0001;

        event eventIn (soul::note_events::NoteOn e)
        {
            active = true;
            keyScaling = TX81Z::keyVelocityMapping (keyVelocitySensitivity, e.velocity);

            let rateScalingFactor = TX81Z::rateScalingFactor (keyRateScaling, e.note);

            attackFactor  = TX81Z::enveloperAttackRate (processor.frequency, attackRate, rateScalingFactor);
            decay1Target  = TX81Z::envelopeDecayLevel (decay1Level);
            decay1Factor  = TX81Z::enveloperDecayRateFactor (processor.frequency, decay1Rate, rateScalingFactor);
            decay2Factor  = TX81Z::enveloperDecayRateFactor (processor.frequency, decay2Rate, rateScalingFactor);
            releaseFactor = TX81Z::envelopeReleaseRateFactor (processor.frequency, releaseRate, rateScalingFactor);
        }

        event eventIn (soul::note_events::NoteOff e)
        {
            active = false;
        }

        void run()
        {
            var value = 0.0;

            loop
            {
                while (! active)
                {
                    audioOut << 0.0f;
                    advance();
                }

                // Attack
                while (active && value < 1.0)
                {
                    // Model as linear attack - not accurate (more like an S shape in the instrument)
                    value += attackFactor;
                    audioOut << keyScaling * float (value);
                    advance();
                }

                value = 1.0;

                // Decay1
                while (active && value > decay1Target)
                {
                    value *= decay1Factor;
                    audioOut << keyScaling * float (value);
                    advance();
                }

                // Decay2
                while (active)
                {
                    value *= decay2Factor;
                    audioOut << keyScaling * float (value);
                    advance();

                    if (value < envelopeLimit)
                        active = false;
                }

                // Release
                while (! active && value > envelopeLimit)
                {
                    value *= releaseFactor;
                    audioOut << keyScaling * float (value);
                    advance();
                }
            }
        }
    }

    //==============================================================================
    // Remove DC from the output, apply some gain reduction
    processor OutputFilter
    {
        input stream float  audioIn;
        output stream float audioOut;

        void run()
        {
            let gain = soul::dBtoGain (-6.0f);

            float previousIn, previousOut;

            loop
            {
                let y = audioIn - previousIn + 0.997f * previousOut;
                previousIn = audioIn;
                previousOut = y;

                audioOut << gain * y;
                advance();
            }
        }
    }

    //==============================================================================
    // The voice allocator supports an offset as some patches are pitched up/down
    processor PolyVoiceAllocator (int noteOffset, int voiceCount)
    {
        input event (soul::note_events::NoteOn,
                     soul::note_events::NoteOff,
                     soul::note_events::PitchBend) eventIn;


        output event (soul::note_events::NoteOn,
                      soul::note_events::NoteOff,
                      soul::note_events::PitchBend) eventOut[voiceCount];

        event eventIn (soul::note_events::NoteOn e)
        {
            e.note = clamp (e.note + noteOffset, 24.0f, 127.0f);

            wrap<voiceCount> allocatedVoice = 0;
            var allocatedVoiceAge = voiceInfo[allocatedVoice].voiceAge;

            // Find the oldest voice to reuse
            for (int i = 1; i < voiceCount; i++)
            {
                let age = voiceInfo.at(i).voiceAge;

                if (age < allocatedVoiceAge)
                {
                    allocatedVoiceAge = age;
                    allocatedVoice = wrap<voiceCount>(i);
                }
            }

            // Update the VoiceInfo for our chosen voice
            voiceInfo[allocatedVoice].channel  = e.channel;
            voiceInfo[allocatedVoice].note     = e.note;
            voiceInfo[allocatedVoice].voiceAge = nextAllocatedVoiceAge++;

            // Send the note on to the voice
            eventOut[allocatedVoice] << e;
        }

        event eventIn (soul::note_events::NoteOff e)
        {
            e.note = clamp (e.note + noteOffset, 24.0f, 127.0f);

            // Release all voices associated with this note/channel
            wrap<voiceCount> voice = 0;

            loop (voiceCount)
            {
                if (voiceInfo[voice].channel == e.channel
                    && voiceInfo[voice].note == e.note)
                {
                    // Mark the voice as being unused
                    voiceInfo[voice].voiceAge = nextUnallocatedVoiceAge++;
                    eventOut[voice] << e;
                }

                ++voice;
            }
        }

        event eventIn (soul::note_events::PitchBend e)
        {
            // Forward the pitch bend to all notes on this channel
            wrap<voiceCount> voice = 0;

            loop (voiceCount)
            {
                if (voiceInfo[voice].channel == e.channel)
                    eventOut[voice] << e;

                voice++;
            }
        }

        struct VoiceInfo
        {
            bool active;
            float note;
            int channel, voiceAge;
        }

        int nextAllocatedVoiceAge   = 1000000000;
        int nextUnallocatedVoiceAge = 1;

        VoiceInfo[voiceCount] voiceInfo;
    }
}', 'TX81Z.soul',
        (select id from soulpatches where name = 'LatelyBass'));

