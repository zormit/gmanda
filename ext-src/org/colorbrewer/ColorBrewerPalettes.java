package org.colorbrewer;

/**
 * <P>
 * Copyright (c) 2002 Cynthia Brewer, Mark Harrower, and The Pennsylvania State
 * University. All rights reserved.
 * <P>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <OL>
 * <LI>Redistributions as source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * <LI>The end-user documentation included with the redistribution, if any, must
 * include the following acknowledgment:
 * <P>
 * <I>This product includes color specifications and designs developed by
 * Cynthia Brewer (http://colorbrewer.org/)</I>.
 * <P>
 * Alternately, this acknowledgment may appear in the software itself, if and
 * wherever such third-party acknowledgments normally appear.
 * <LI>The name "ColorBrewer" must not be used to endorse or promote products
 * derived from this software without prior written permission. For written
 * permission, please contact Cynthia Brewer at cbrewer@psu.edu.
 * <LI>Products derived from this software may not be called "ColorBrewer", nor
 * may "ColorBrewer" appear in their name, without prior written permission of
 * Cynthia Brewer.
 * </OL>
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL CYNTHIA
 * BREWER, MARK HARROWER, OR THE PENNSYLVANIA STATE UNIVERSITY BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * These colors were Javafied by Christopher Oezbek
 * 
 */
public class ColorBrewerPalettes {
 
	public static String getColorFromInt(String[] palette, int i, int max) {

		int color = i / (max / palette.length);

		if (color < 0)
			return palette[0];
		if (color >= palette.length)
			return palette[palette.length - 1];
		return palette[color];
	}
	
	
	public static String aliceblue = "#f0f8ff";
	public static String aliceblueFont = "#000000";
	public static String antiquewhite = "#faebd7";
	public static String antiquewhiteFont = "#000000";
	public static String antiquewhite1 = "#ffefdb";
	public static String antiquewhite1Font = "#000000";
	public static String antiquewhite2 = "#eedfcc";
	public static String antiquewhite2Font = "#000000";
	public static String antiquewhite3 = "#cdc0b0";
	public static String antiquewhite3Font = "#000000";

	public static String antiquewhite4 = "#8b8378";
	public static String antiquewhite4Font = "#000000";
	public static String aquamarine = "#7fffd4";
	public static String aquamarineFont = "#000000";
	public static String aquamarine1 = "#7fffd4";
	public static String aquamarine1Font = "#000000";
	public static String aquamarine2 = "#76eec6";
	public static String aquamarine2Font = "#000000";
	public static String aquamarine3 = "#66cdaa";
	public static String aquamarine3Font = "#000000";

	public static String aquamarine4 = "#458b74";
	public static String aquamarine4Font = "#000000";
	public static String azure = "#f0ffff";
	public static String azureFont = "#000000";
	public static String azure1 = "#f0ffff";
	public static String azure1Font = "#000000";
	public static String azure2 = "#e0eeee";
	public static String azure2Font = "#000000";
	public static String azure3 = "#c1cdcd";
	public static String azure3Font = "#000000";

	public static String azure4 = "#838b8b";
	public static String azure4Font = "#000000";
	public static String beige = "#f5f5dc";
	public static String beigeFont = "#000000";
	public static String bisque = "#ffe4c4";
	public static String bisqueFont = "#000000";
	public static String bisque1 = "#ffe4c4";
	public static String bisque1Font = "#000000";
	public static String bisque2 = "#eed5b7";
	public static String bisque2Font = "#000000";

	public static String bisque3 = "#cdb79e";
	public static String bisque3Font = "#000000";
	public static String bisque4 = "#8b7d6b";
	public static String bisque4Font = "#000000";
	public static String black = "#000000";
	public static String blackFont = "#FFFFFF";
	public static String blanchedalmond = "#ffebcd";
	public static String blanchedalmondFont = "#000000";
	public static String blue = "#0000ff";
	public static String blueFont = "#000000";

	public static String blue1 = "#0000ff";
	public static String blue1Font = "#000000";
	public static String blue2 = "#0000ee";
	public static String blue2Font = "#000000";
	public static String blue3 = "#0000cd";
	public static String blue3Font = "#000000";
	public static String blue4 = "#00008b";
	public static String blue4Font = "#000000";
	public static String blueviolet = "#8a2be2";
	public static String bluevioletFont = "#000000";

	public static String brown = "#a52a2a";
	public static String brownFont = "#000000";
	public static String brown1 = "#ff4040";
	public static String brown1Font = "#000000";
	public static String brown2 = "#ee3b3b";
	public static String brown2Font = "#000000";
	public static String brown3 = "#cd3333";
	public static String brown3Font = "#000000";
	public static String brown4 = "#8b2323";
	public static String brown4Font = "#000000";

	public static String burlywood = "#deb887";
	public static String burlywoodFont = "#000000";
	public static String burlywood1 = "#ffd39b";
	public static String burlywood1Font = "#000000";
	public static String burlywood2 = "#eec591";
	public static String burlywood2Font = "#000000";
	public static String burlywood3 = "#cdaa7d";
	public static String burlywood3Font = "#000000";
	public static String burlywood4 = "#8b7355";
	public static String burlywood4Font = "#000000";

	public static String cadetblue = "#5f9ea0";
	public static String cadetblueFont = "#000000";
	public static String cadetblue1 = "#98f5ff";
	public static String cadetblue1Font = "#000000";
	public static String cadetblue2 = "#8ee5ee";
	public static String cadetblue2Font = "#000000";
	public static String cadetblue3 = "#7ac5cd";
	public static String cadetblue3Font = "#000000";
	public static String cadetblue4 = "#53868b";
	public static String cadetblue4Font = "#000000";

	public static String chartreuse = "#7fff00";
	public static String chartreuseFont = "#000000";
	public static String chartreuse1 = "#7fff00";
	public static String chartreuse1Font = "#000000";
	public static String chartreuse2 = "#76ee00";
	public static String chartreuse2Font = "#000000";
	public static String chartreuse3 = "#66cd00";
	public static String chartreuse3Font = "#000000";
	public static String chartreuse4 = "#458b00";
	public static String chartreuse4Font = "#000000";

	public static String chocolate = "#d2691e";
	public static String chocolateFont = "#000000";
	public static String chocolate1 = "#ff7f24";
	public static String chocolate1Font = "#000000";
	public static String chocolate2 = "#ee7621";
	public static String chocolate2Font = "#000000";
	public static String chocolate3 = "#cd661d";
	public static String chocolate3Font = "#000000";
	public static String chocolate4 = "#8b4513";
	public static String chocolate4Font = "#000000";

	public static String coral = "#ff7f50";
	public static String coralFont = "#000000";
	public static String coral1 = "#ff7256";
	public static String coral1Font = "#000000";
	public static String coral2 = "#ee6a50";
	public static String coral2Font = "#000000";
	public static String coral3 = "#cd5b45";
	public static String coral3Font = "#000000";
	public static String coral4 = "#8b3e2f";
	public static String coral4Font = "#000000";

	public static String cornflowerblue = "#6495ed";
	public static String cornflowerblueFont = "#000000";
	public static String cornsilk = "#fff8dc";
	public static String cornsilkFont = "#000000";
	public static String cornsilk1 = "#fff8dc";
	public static String cornsilk1Font = "#000000";
	public static String cornsilk2 = "#eee8cd";
	public static String cornsilk2Font = "#000000";
	public static String cornsilk3 = "#cdc8b1";
	public static String cornsilk3Font = "#000000";

	public static String cornsilk4 = "#8b8878";
	public static String cornsilk4Font = "#000000";
	public static String crimson = "#dc143c";
	public static String crimsonFont = "#000000";
	public static String cyan = "#00ffff";
	public static String cyanFont = "#000000";
	public static String cyan1 = "#00ffff";
	public static String cyan1Font = "#000000";
	public static String cyan2 = "#00eeee";
	public static String cyan2Font = "#000000";

	public static String cyan3 = "#00cdcd";
	public static String cyan3Font = "#000000";
	public static String cyan4 = "#008b8b";
	public static String cyan4Font = "#000000";
	public static String darkgoldenrod = "#b8860b";
	public static String darkgoldenrodFont = "#000000";
	public static String darkgoldenrod1 = "#ffb90f";
	public static String darkgoldenrod1Font = "#000000";
	public static String darkgoldenrod2 = "#eead0e";
	public static String darkgoldenrod2Font = "#000000";

	public static String darkgoldenrod3 = "#cd950c";
	public static String darkgoldenrod3Font = "#000000";
	public static String darkgoldenrod4 = "#8b6508";
	public static String darkgoldenrod4Font = "#000000";
	public static String darkgreen = "#006400";
	public static String darkgreenFont = "#FFFFFF";
	public static String darkkhaki = "#bdb76b";
	public static String darkkhakiFont = "#000000";
	public static String darkolivegreen = "#556b2f";
	public static String darkolivegreenFont = "#FFFFFF";

	public static String darkolivegreen1 = "#caff70";
	public static String darkolivegreen1Font = "#000000";
	public static String darkolivegreen2 = "#bcee68";
	public static String darkolivegreen2Font = "#000000";
	public static String darkolivegreen3 = "#a2cd5a";
	public static String darkolivegreen3Font = "#000000";
	public static String darkolivegreen4 = "#6e8b3d";
	public static String darkolivegreen4Font = "#000000";
	public static String darkorange = "#ff8c00";
	public static String darkorangeFont = "#000000";

	public static String darkorange1 = "#ff7f00";
	public static String darkorange1Font = "#000000";
	public static String darkorange2 = "#ee7600";
	public static String darkorange2Font = "#000000";
	public static String darkorange3 = "#cd6600";
	public static String darkorange3Font = "#000000";
	public static String darkorange4 = "#8b4500";
	public static String darkorange4Font = "#000000";
	public static String darkorchid = "#9932cc";
	public static String darkorchidFont = "#000000";

	public static String darkorchid1 = "#bf3eff";
	public static String darkorchid1Font = "#000000";
	public static String darkorchid2 = "#b23aee";
	public static String darkorchid2Font = "#000000";
	public static String darkorchid3 = "#9a32cd";
	public static String darkorchid3Font = "#000000";
	public static String darkorchid4 = "#68228b";
	public static String darkorchid4Font = "#000000";
	public static String darksalmon = "#e9967a";
	public static String darksalmonFont = "#000000";

	public static String darkseagreen = "#8fbc8f";
	public static String darkseagreenFont = "#000000";
	public static String darkseagreen1 = "#c1ffc1";
	public static String darkseagreen1Font = "#000000";
	public static String darkseagreen2 = "#b4eeb4";
	public static String darkseagreen2Font = "#000000";
	public static String darkseagreen3 = "#9bcd9b";
	public static String darkseagreen3Font = "#000000";
	public static String darkseagreen4 = "#698b69";
	public static String darkseagreen4Font = "#000000";

	public static String darkslateblue = "#483d8b";
	public static String darkslateblueFont = "#000000";
	public static String darkslategray = "#2f4f4f";
	public static String darkslategrayFont = "#FFFFFF";
	public static String darkslategray1 = "#97ffff";
	public static String darkslategray1Font = "#000000";
	public static String darkslategray2 = "#8deeee";
	public static String darkslategray2Font = "#000000";
	public static String darkslategray3 = "#79cdcd";
	public static String darkslategray3Font = "#000000";

	public static String darkslategray4 = "#528b8b";
	public static String darkslategray4Font = "#000000";
	public static String darkslategrey = "#2f4f4f";
	public static String darkslategreyFont = "#FFFFFF";
	public static String darkturquoise = "#00ced1";
	public static String darkturquoiseFont = "#000000";
	public static String darkviolet = "#9400d3";
	public static String darkvioletFont = "#000000";
	public static String deeppink = "#ff1493";
	public static String deeppinkFont = "#000000";

	public static String deeppink1 = "#ff1493";
	public static String deeppink1Font = "#000000";
	public static String deeppink2 = "#ee1289";
	public static String deeppink2Font = "#000000";
	public static String deeppink3 = "#cd1076";
	public static String deeppink3Font = "#000000";
	public static String deeppink4 = "#8b0a50";
	public static String deeppink4Font = "#000000";
	public static String deepskyblue = "#00bfff";
	public static String deepskyblueFont = "#000000";

	public static String deepskyblue1 = "#00bfff";
	public static String deepskyblue1Font = "#000000";
	public static String deepskyblue2 = "#00b2ee";
	public static String deepskyblue2Font = "#000000";
	public static String deepskyblue3 = "#009acd";
	public static String deepskyblue3Font = "#000000";
	public static String deepskyblue4 = "#00688b";
	public static String deepskyblue4Font = "#000000";
	public static String dimgray = "#696969";
	public static String dimgrayFont = "#FFFFFF";

	public static String dimgrey = "#696969";
	public static String dimgreyFont = "#FFFFFF";
	public static String dodgerblue = "#1e90ff";
	public static String dodgerblueFont = "#000000";
	public static String dodgerblue1 = "#1e90ff";
	public static String dodgerblue1Font = "#000000";
	public static String dodgerblue2 = "#1c86ee";
	public static String dodgerblue2Font = "#000000";
	public static String dodgerblue3 = "#1874cd";
	public static String dodgerblue3Font = "#000000";

	public static String dodgerblue4 = "#104e8b";
	public static String dodgerblue4Font = "#000000";
	public static String firebrick = "#b22222";
	public static String firebrickFont = "#000000";
	public static String firebrick1 = "#ff3030";
	public static String firebrick1Font = "#000000";
	public static String firebrick2 = "#ee2c2c";
	public static String firebrick2Font = "#000000";
	public static String firebrick3 = "#cd2626";
	public static String firebrick3Font = "#000000";

	public static String firebrick4 = "#8b1a1a";
	public static String firebrick4Font = "#000000";
	public static String floralwhite = "#fffaf0";
	public static String floralwhiteFont = "#000000";
	public static String forestgreen = "#228b22";
	public static String forestgreenFont = "#000000";
	public static String gainsboro = "#dcdcdc";
	public static String gainsboroFont = "#000000";
	public static String ghostwhite = "#f8f8ff";
	public static String ghostwhiteFont = "#000000";

	public static String gold = "#ffd700";
	public static String goldFont = "#000000";
	public static String gold1 = "#ffd700";
	public static String gold1Font = "#000000";
	public static String gold2 = "#eec900";
	public static String gold2Font = "#000000";
	public static String gold3 = "#cdad00";
	public static String gold3Font = "#000000";
	public static String gold4 = "#8b7500";
	public static String gold4Font = "#000000";

	public static String goldenrod = "#daa520";
	public static String goldenrodFont = "#000000";
	public static String goldenrod1 = "#ffc125";
	public static String goldenrod1Font = "#000000";
	public static String goldenrod2 = "#eeb422";
	public static String goldenrod2Font = "#000000";
	public static String goldenrod3 = "#cd9b1d";
	public static String goldenrod3Font = "#000000";
	public static String goldenrod4 = "#8b6914";
	public static String goldenrod4Font = "#000000";

	public static String gray = "#c0c0c0";
	public static String grayFont = "#000000";
	public static String gray0 = "#000000";
	public static String gray0Font = "#FFFFFF";
	public static String gray1 = "#030303";
	public static String gray1Font = "#FFFFFF";
	public static String gray2 = "#050505";
	public static String gray2Font = "#FFFFFF";
	public static String gray3 = "#080808";
	public static String gray3Font = "#FFFFFF";

	public static String gray4 = "#0a0a0a";
	public static String gray4Font = "#FFFFFF";
	public static String gray5 = "#0d0d0d";
	public static String gray5Font = "#FFFFFF";
	public static String gray6 = "#0f0f0f";
	public static String gray6Font = "#FFFFFF";
	public static String gray7 = "#121212";
	public static String gray7Font = "#FFFFFF";
	public static String gray8 = "#141414";
	public static String gray8Font = "#FFFFFF";

	public static String gray9 = "#171717";
	public static String gray9Font = "#FFFFFF";
	public static String gray10 = "#1a1a1a";
	public static String gray10Font = "#FFFFFF";
	public static String gray11 = "#1c1c1c";
	public static String gray11Font = "#FFFFFF";
	public static String gray12 = "#1f1f1f";
	public static String gray12Font = "#FFFFFF";
	public static String gray13 = "#212121";
	public static String gray13Font = "#FFFFFF";

	public static String gray14 = "#242424";
	public static String gray14Font = "#FFFFFF";
	public static String gray15 = "#262626";
	public static String gray15Font = "#FFFFFF";
	public static String gray16 = "#292929";
	public static String gray16Font = "#FFFFFF";
	public static String gray17 = "#2b2b2b";
	public static String gray17Font = "#FFFFFF";
	public static String gray18 = "#2e2e2e";
	public static String gray18Font = "#FFFFFF";

	public static String gray19 = "#303030";
	public static String gray19Font = "#FFFFFF";
	public static String gray20 = "#333333";
	public static String gray20Font = "#FFFFFF";
	public static String gray21 = "#363636";
	public static String gray21Font = "#FFFFFF";
	public static String gray22 = "#383838";
	public static String gray22Font = "#FFFFFF";
	public static String gray23 = "#3b3b3b";
	public static String gray23Font = "#FFFFFF";

	public static String gray24 = "#3d3d3d";
	public static String gray24Font = "#FFFFFF";
	public static String gray25 = "#404040";
	public static String gray25Font = "#FFFFFF";
	public static String gray26 = "#424242";
	public static String gray26Font = "#FFFFFF";
	public static String gray27 = "#454545";
	public static String gray27Font = "#FFFFFF";
	public static String gray28 = "#474747";
	public static String gray28Font = "#FFFFFF";

	public static String gray29 = "#4a4a4a";
	public static String gray29Font = "#FFFFFF";
	public static String gray30 = "#4d4d4d";
	public static String gray30Font = "#FFFFFF";
	public static String gray31 = "#4f4f4f";
	public static String gray31Font = "#FFFFFF";
	public static String gray32 = "#525252";
	public static String gray32Font = "#FFFFFF";
	public static String gray33 = "#545454";
	public static String gray33Font = "#FFFFFF";

	public static String gray34 = "#575757";
	public static String gray34Font = "#FFFFFF";
	public static String gray35 = "#595959";
	public static String gray35Font = "#FFFFFF";
	public static String gray36 = "#5c5c5c";
	public static String gray36Font = "#FFFFFF";
	public static String gray37 = "#5e5e5e";
	public static String gray37Font = "#FFFFFF";
	public static String gray38 = "#616161";
	public static String gray38Font = "#FFFFFF";

	public static String gray39 = "#636363";
	public static String gray39Font = "#FFFFFF";
	public static String gray40 = "#666666";
	public static String gray40Font = "#FFFFFF";
	public static String gray41 = "#696969";
	public static String gray41Font = "#FFFFFF";
	public static String gray42 = "#6b6b6b";
	public static String gray42Font = "#FFFFFF";
	public static String gray43 = "#6e6e6e";
	public static String gray43Font = "#FFFFFF";

	public static String gray44 = "#707070";
	public static String gray44Font = "#FFFFFF";
	public static String gray45 = "#737373";
	public static String gray45Font = "#FFFFFF";
	public static String gray46 = "#757575";
	public static String gray46Font = "#FFFFFF";
	public static String gray47 = "#787878";
	public static String gray47Font = "#FFFFFF";
	public static String gray48 = "#7a7a7a";
	public static String gray48Font = "#FFFFFF";

	public static String gray49 = "#7d7d7d";
	public static String gray49Font = "#FFFFFF";
	public static String gray50 = "#7f7f7f";
	public static String gray50Font = "#FFFFFF";
	public static String gray51 = "#828282";
	public static String gray51Font = "#FFFFFF";
	public static String gray52 = "#858585";
	public static String gray52Font = "#000000";
	public static String gray53 = "#878787";
	public static String gray53Font = "#000000";

	public static String gray54 = "#8a8a8a";
	public static String gray54Font = "#000000";
	public static String gray55 = "#8c8c8c";
	public static String gray55Font = "#000000";
	public static String gray56 = "#8f8f8f";
	public static String gray56Font = "#000000";
	public static String gray57 = "#919191";
	public static String gray57Font = "#000000";
	public static String gray58 = "#949494";
	public static String gray58Font = "#000000";

	public static String gray59 = "#969696";
	public static String gray59Font = "#000000";
	public static String gray60 = "#999999";
	public static String gray60Font = "#000000";
	public static String gray61 = "#9c9c9c";
	public static String gray61Font = "#000000";
	public static String gray62 = "#9e9e9e";
	public static String gray62Font = "#000000";
	public static String gray63 = "#a1a1a1";
	public static String gray63Font = "#000000";

	public static String gray64 = "#a3a3a3";
	public static String gray64Font = "#000000";
	public static String gray65 = "#a6a6a6";
	public static String gray65Font = "#000000";
	public static String gray66 = "#a8a8a8";
	public static String gray66Font = "#000000";
	public static String gray67 = "#ababab";
	public static String gray67Font = "#000000";
	public static String gray68 = "#adadad";
	public static String gray68Font = "#000000";

	public static String gray69 = "#b0b0b0";
	public static String gray69Font = "#000000";
	public static String gray70 = "#b3b3b3";
	public static String gray70Font = "#000000";
	public static String gray71 = "#b5b5b5";
	public static String gray71Font = "#000000";
	public static String gray72 = "#b8b8b8";
	public static String gray72Font = "#000000";
	public static String gray73 = "#bababa";
	public static String gray73Font = "#000000";

	public static String gray74 = "#bdbdbd";
	public static String gray74Font = "#000000";
	public static String gray75 = "#bfbfbf";
	public static String gray75Font = "#000000";
	public static String gray76 = "#c2c2c2";
	public static String gray76Font = "#000000";
	public static String gray77 = "#c4c4c4";
	public static String gray77Font = "#000000";
	public static String gray78 = "#c7c7c7";
	public static String gray78Font = "#000000";

	public static String gray79 = "#c9c9c9";
	public static String gray79Font = "#000000";
	public static String gray80 = "#cccccc";
	public static String gray80Font = "#000000";
	public static String gray81 = "#cfcfcf";
	public static String gray81Font = "#000000";
	public static String gray82 = "#d1d1d1";
	public static String gray82Font = "#000000";
	public static String gray83 = "#d4d4d4";
	public static String gray83Font = "#000000";

	public static String gray84 = "#d6d6d6";
	public static String gray84Font = "#000000";
	public static String gray85 = "#d9d9d9";
	public static String gray85Font = "#000000";
	public static String gray86 = "#dbdbdb";
	public static String gray86Font = "#000000";
	public static String gray87 = "#dedede";
	public static String gray87Font = "#000000";
	public static String gray88 = "#e0e0e0";
	public static String gray88Font = "#000000";

	public static String gray89 = "#e3e3e3";
	public static String gray89Font = "#000000";
	public static String gray90 = "#e5e5e5";
	public static String gray90Font = "#000000";
	public static String gray91 = "#e8e8e8";
	public static String gray91Font = "#000000";
	public static String gray92 = "#ebebeb";
	public static String gray92Font = "#000000";
	public static String gray93 = "#ededed";
	public static String gray93Font = "#000000";

	public static String gray94 = "#f0f0f0";
	public static String gray94Font = "#000000";
	public static String gray95 = "#f2f2f2";
	public static String gray95Font = "#000000";
	public static String gray96 = "#f5f5f5";
	public static String gray96Font = "#000000";
	public static String gray97 = "#f7f7f7";
	public static String gray97Font = "#000000";
	public static String gray98 = "#fafafa";
	public static String gray98Font = "#000000";

	public static String gray99 = "#fcfcfc";
	public static String gray99Font = "#000000";
	public static String gray100 = "#ffffff";
	public static String gray100Font = "#000000";
	public static String green = "#00ff00";
	public static String greenFont = "#000000";
	public static String green1 = "#00ff00";
	public static String green1Font = "#000000";
	public static String green2 = "#00ee00";
	public static String green2Font = "#000000";

	public static String green3 = "#00cd00";
	public static String green3Font = "#000000";
	public static String green4 = "#008b00";
	public static String green4Font = "#000000";
	public static String greenyellow = "#adff2f";
	public static String greenyellowFont = "#000000";
	public static String grey = "#c0c0c0";
	public static String greyFont = "#000000";
	public static String grey0 = "#000000";
	public static String grey0Font = "#FFFFFF";

	public static String grey1 = "#030303";
	public static String grey1Font = "#FFFFFF";
	public static String grey2 = "#050505";
	public static String grey2Font = "#FFFFFF";
	public static String grey3 = "#080808";
	public static String grey3Font = "#FFFFFF";
	public static String grey4 = "#0a0a0a";
	public static String grey4Font = "#FFFFFF";
	public static String grey5 = "#0d0d0d";
	public static String grey5Font = "#FFFFFF";

	public static String grey6 = "#0f0f0f";
	public static String grey6Font = "#FFFFFF";
	public static String grey7 = "#121212";
	public static String grey7Font = "#FFFFFF";
	public static String grey8 = "#141414";
	public static String grey8Font = "#FFFFFF";
	public static String grey9 = "#171717";
	public static String grey9Font = "#FFFFFF";
	public static String grey10 = "#1a1a1a";
	public static String grey10Font = "#FFFFFF";

	public static String grey11 = "#1c1c1c";
	public static String grey11Font = "#FFFFFF";
	public static String grey12 = "#1f1f1f";
	public static String grey12Font = "#FFFFFF";
	public static String grey13 = "#212121";
	public static String grey13Font = "#FFFFFF";
	public static String grey14 = "#242424";
	public static String grey14Font = "#FFFFFF";
	public static String grey15 = "#262626";
	public static String grey15Font = "#FFFFFF";

	public static String grey16 = "#292929";
	public static String grey16Font = "#FFFFFF";
	public static String grey17 = "#2b2b2b";
	public static String grey17Font = "#FFFFFF";
	public static String grey18 = "#2e2e2e";
	public static String grey18Font = "#FFFFFF";
	public static String grey19 = "#303030";
	public static String grey19Font = "#FFFFFF";
	public static String grey20 = "#333333";
	public static String grey20Font = "#FFFFFF";

	public static String grey21 = "#363636";
	public static String grey21Font = "#FFFFFF";
	public static String grey22 = "#383838";
	public static String grey22Font = "#FFFFFF";
	public static String grey23 = "#3b3b3b";
	public static String grey23Font = "#FFFFFF";
	public static String grey24 = "#3d3d3d";
	public static String grey24Font = "#FFFFFF";
	public static String grey25 = "#404040";
	public static String grey25Font = "#FFFFFF";

	public static String grey26 = "#424242";
	public static String grey26Font = "#FFFFFF";
	public static String grey27 = "#454545";
	public static String grey27Font = "#FFFFFF";
	public static String grey28 = "#474747";
	public static String grey28Font = "#FFFFFF";
	public static String grey29 = "#4a4a4a";
	public static String grey29Font = "#FFFFFF";
	public static String grey30 = "#4d4d4d";
	public static String grey30Font = "#FFFFFF";

	public static String grey31 = "#4f4f4f";
	public static String grey31Font = "#FFFFFF";
	public static String grey32 = "#525252";
	public static String grey32Font = "#FFFFFF";
	public static String grey33 = "#545454";
	public static String grey33Font = "#FFFFFF";
	public static String grey34 = "#575757";
	public static String grey34Font = "#FFFFFF";
	public static String grey35 = "#595959";
	public static String grey35Font = "#FFFFFF";

	public static String grey36 = "#5c5c5c";
	public static String grey36Font = "#FFFFFF";
	public static String grey37 = "#5e5e5e";
	public static String grey37Font = "#FFFFFF";
	public static String grey38 = "#616161";
	public static String grey38Font = "#FFFFFF";
	public static String grey39 = "#636363";
	public static String grey39Font = "#FFFFFF";
	public static String grey40 = "#666666";
	public static String grey40Font = "#FFFFFF";

	public static String grey41 = "#696969";
	public static String grey41Font = "#FFFFFF";
	public static String grey42 = "#6b6b6b";
	public static String grey42Font = "#FFFFFF";
	public static String grey43 = "#6e6e6e";
	public static String grey43Font = "#FFFFFF";
	public static String grey44 = "#707070";
	public static String grey44Font = "#FFFFFF";
	public static String grey45 = "#737373";
	public static String grey45Font = "#FFFFFF";

	public static String grey46 = "#757575";
	public static String grey46Font = "#FFFFFF";
	public static String grey47 = "#787878";
	public static String grey47Font = "#FFFFFF";
	public static String grey48 = "#7a7a7a";
	public static String grey48Font = "#FFFFFF";
	public static String grey49 = "#7d7d7d";
	public static String grey49Font = "#FFFFFF";
	public static String grey50 = "#7f7f7f";
	public static String grey50Font = "#FFFFFF";

	public static String grey51 = "#828282";
	public static String grey51Font = "#FFFFFF";
	public static String grey52 = "#858585";
	public static String grey52Font = "#000000";
	public static String grey53 = "#878787";
	public static String grey53Font = "#000000";
	public static String grey54 = "#8a8a8a";
	public static String grey54Font = "#000000";
	public static String grey55 = "#8c8c8c";
	public static String grey55Font = "#000000";

	public static String grey56 = "#8f8f8f";
	public static String grey56Font = "#000000";
	public static String grey57 = "#919191";
	public static String grey57Font = "#000000";
	public static String grey58 = "#949494";
	public static String grey58Font = "#000000";
	public static String grey59 = "#969696";
	public static String grey59Font = "#000000";
	public static String grey60 = "#999999";
	public static String grey60Font = "#000000";

	public static String grey61 = "#9c9c9c";
	public static String grey61Font = "#000000";
	public static String grey62 = "#9e9e9e";
	public static String grey62Font = "#000000";
	public static String grey63 = "#a1a1a1";
	public static String grey63Font = "#000000";
	public static String grey64 = "#a3a3a3";
	public static String grey64Font = "#000000";
	public static String grey65 = "#a6a6a6";
	public static String grey65Font = "#000000";

	public static String grey66 = "#a8a8a8";
	public static String grey66Font = "#000000";
	public static String grey67 = "#ababab";
	public static String grey67Font = "#000000";
	public static String grey68 = "#adadad";
	public static String grey68Font = "#000000";
	public static String grey69 = "#b0b0b0";
	public static String grey69Font = "#000000";
	public static String grey70 = "#b3b3b3";
	public static String grey70Font = "#000000";

	public static String grey71 = "#b5b5b5";
	public static String grey71Font = "#000000";
	public static String grey72 = "#b8b8b8";
	public static String grey72Font = "#000000";
	public static String grey73 = "#bababa";
	public static String grey73Font = "#000000";
	public static String grey74 = "#bdbdbd";
	public static String grey74Font = "#000000";
	public static String grey75 = "#bfbfbf";
	public static String grey75Font = "#000000";

	public static String grey76 = "#c2c2c2";
	public static String grey76Font = "#000000";
	public static String grey77 = "#c4c4c4";
	public static String grey77Font = "#000000";
	public static String grey78 = "#c7c7c7";
	public static String grey78Font = "#000000";
	public static String grey79 = "#c9c9c9";
	public static String grey79Font = "#000000";
	public static String grey80 = "#cccccc";
	public static String grey80Font = "#000000";

	public static String grey81 = "#cfcfcf";
	public static String grey81Font = "#000000";
	public static String grey82 = "#d1d1d1";
	public static String grey82Font = "#000000";
	public static String grey83 = "#d4d4d4";
	public static String grey83Font = "#000000";
	public static String grey84 = "#d6d6d6";
	public static String grey84Font = "#000000";
	public static String grey85 = "#d9d9d9";
	public static String grey85Font = "#000000";

	public static String grey86 = "#dbdbdb";
	public static String grey86Font = "#000000";
	public static String grey87 = "#dedede";
	public static String grey87Font = "#000000";
	public static String grey88 = "#e0e0e0";
	public static String grey88Font = "#000000";
	public static String grey89 = "#e3e3e3";
	public static String grey89Font = "#000000";
	public static String grey90 = "#e5e5e5";
	public static String grey90Font = "#000000";

	public static String grey91 = "#e8e8e8";
	public static String grey91Font = "#000000";
	public static String grey92 = "#ebebeb";
	public static String grey92Font = "#000000";
	public static String grey93 = "#ededed";
	public static String grey93Font = "#000000";
	public static String grey94 = "#f0f0f0";
	public static String grey94Font = "#000000";
	public static String grey95 = "#f2f2f2";
	public static String grey95Font = "#000000";

	public static String grey96 = "#f5f5f5";
	public static String grey96Font = "#000000";
	public static String grey97 = "#f7f7f7";
	public static String grey97Font = "#000000";
	public static String grey98 = "#fafafa";
	public static String grey98Font = "#000000";
	public static String grey99 = "#fcfcfc";
	public static String grey99Font = "#000000";
	public static String grey100 = "#ffffff";
	public static String grey100Font = "#000000";

	public static String honeydew = "#f0fff0";
	public static String honeydewFont = "#000000";
	public static String honeydew1 = "#f0fff0";
	public static String honeydew1Font = "#000000";
	public static String honeydew2 = "#e0eee0";
	public static String honeydew2Font = "#000000";
	public static String honeydew3 = "#c1cdc1";
	public static String honeydew3Font = "#000000";
	public static String honeydew4 = "#838b83";
	public static String honeydew4Font = "#000000";

	public static String hotpink = "#ff69b4";
	public static String hotpinkFont = "#000000";
	public static String hotpink1 = "#ff6eb4";
	public static String hotpink1Font = "#000000";
	public static String hotpink2 = "#ee6aa7";
	public static String hotpink2Font = "#000000";
	public static String hotpink3 = "#cd6090";
	public static String hotpink3Font = "#000000";
	public static String hotpink4 = "#8b3a62";
	public static String hotpink4Font = "#000000";

	public static String indianred = "#cd5c5c";
	public static String indianredFont = "#000000";
	public static String indianred1 = "#ff6a6a";
	public static String indianred1Font = "#000000";
	public static String indianred2 = "#ee6363";
	public static String indianred2Font = "#000000";
	public static String indianred3 = "#cd5555";
	public static String indianred3Font = "#000000";
	public static String indianred4 = "#8b3a3a";
	public static String indianred4Font = "#000000";

	public static String indigo = "#4b0082";
	public static String indigoFont = "#FFFFFF";
	public static String ivory = "#fffff0";
	public static String ivoryFont = "#000000";
	public static String ivory1 = "#fffff0";
	public static String ivory1Font = "#000000";
	public static String ivory2 = "#eeeee0";
	public static String ivory2Font = "#000000";
	public static String ivory3 = "#cdcdc1";
	public static String ivory3Font = "#000000";

	public static String ivory4 = "#8b8b83";
	public static String ivory4Font = "#000000";
	public static String khaki = "#f0e68c";
	public static String khakiFont = "#000000";
	public static String khaki1 = "#fff68f";
	public static String khaki1Font = "#000000";
	public static String khaki2 = "#eee685";
	public static String khaki2Font = "#000000";
	public static String khaki3 = "#cdc673";
	public static String khaki3Font = "#000000";

	public static String khaki4 = "#8b864e";
	public static String khaki4Font = "#000000";
	public static String lavender = "#e6e6fa";
	public static String lavenderFont = "#000000";
	public static String lavenderblush = "#fff0f5";
	public static String lavenderblushFont = "#000000";
	public static String lavenderblush1 = "#fff0f5";
	public static String lavenderblush1Font = "#000000";
	public static String lavenderblush2 = "#eee0e5";
	public static String lavenderblush2Font = "#000000";

	public static String lavenderblush3 = "#cdc1c5";
	public static String lavenderblush3Font = "#000000";
	public static String lavenderblush4 = "#8b8386";
	public static String lavenderblush4Font = "#000000";
	public static String lawngreen = "#7cfc00";
	public static String lawngreenFont = "#000000";
	public static String lemonchiffon = "#fffacd";
	public static String lemonchiffonFont = "#000000";
	public static String lemonchiffon1 = "#fffacd";
	public static String lemonchiffon1Font = "#000000";

	public static String lemonchiffon2 = "#eee9bf";
	public static String lemonchiffon2Font = "#000000";
	public static String lemonchiffon3 = "#cdc9a5";
	public static String lemonchiffon3Font = "#000000";
	public static String lemonchiffon4 = "#8b8970";
	public static String lemonchiffon4Font = "#000000";
	public static String lightblue = "#add8e6";
	public static String lightblueFont = "#000000";
	public static String lightblue1 = "#bfefff";
	public static String lightblue1Font = "#000000";

	public static String lightblue2 = "#b2dfee";
	public static String lightblue2Font = "#000000";
	public static String lightblue3 = "#9ac0cd";
	public static String lightblue3Font = "#000000";
	public static String lightblue4 = "#68838b";
	public static String lightblue4Font = "#000000";
	public static String lightcoral = "#f08080";
	public static String lightcoralFont = "#000000";
	public static String lightcyan = "#e0ffff";
	public static String lightcyanFont = "#000000";

	public static String lightcyan1 = "#e0ffff";
	public static String lightcyan1Font = "#000000";
	public static String lightcyan2 = "#d1eeee";
	public static String lightcyan2Font = "#000000";
	public static String lightcyan3 = "#b4cdcd";
	public static String lightcyan3Font = "#000000";
	public static String lightcyan4 = "#7a8b8b";
	public static String lightcyan4Font = "#000000";
	public static String lightgoldenrod = "#eedd82";
	public static String lightgoldenrodFont = "#000000";

	public static String lightgoldenrod1 = "#ffec8b";
	public static String lightgoldenrod1Font = "#000000";
	public static String lightgoldenrod2 = "#eedc82";
	public static String lightgoldenrod2Font = "#000000";
	public static String lightgoldenrod3 = "#cdbe70";
	public static String lightgoldenrod3Font = "#000000";
	public static String lightgoldenrod4 = "#8b814c";
	public static String lightgoldenrod4Font = "#000000";
	public static String lightgoldenrodyellow = "#fafad2";
	public static String lightgoldenrodyellowFont = "#000000";

	public static String lightgray = "#d3d3d3";
	public static String lightgrayFont = "#000000";
	public static String lightgrey = "#d3d3d3";
	public static String lightgreyFont = "#000000";
	public static String lightpink = "#ffb6c1";
	public static String lightpinkFont = "#000000";
	public static String lightpink1 = "#ffaeb9";
	public static String lightpink1Font = "#000000";
	public static String lightpink2 = "#eea2ad";
	public static String lightpink2Font = "#000000";

	public static String lightpink3 = "#cd8c95";
	public static String lightpink3Font = "#000000";
	public static String lightpink4 = "#8b5f65";
	public static String lightpink4Font = "#000000";
	public static String lightsalmon = "#ffa07a";
	public static String lightsalmonFont = "#000000";
	public static String lightsalmon1 = "#ffa07a";
	public static String lightsalmon1Font = "#000000";
	public static String lightsalmon2 = "#ee9572";
	public static String lightsalmon2Font = "#000000";

	public static String lightsalmon3 = "#cd8162";
	public static String lightsalmon3Font = "#000000";
	public static String lightsalmon4 = "#8b5742";
	public static String lightsalmon4Font = "#000000";
	public static String lightseagreen = "#20b2aa";
	public static String lightseagreenFont = "#000000";
	public static String lightskyblue = "#87cefa";
	public static String lightskyblueFont = "#000000";
	public static String lightskyblue1 = "#b0e2ff";
	public static String lightskyblue1Font = "#000000";

	public static String lightskyblue2 = "#a4d3ee";
	public static String lightskyblue2Font = "#000000";
	public static String lightskyblue3 = "#8db6cd";
	public static String lightskyblue3Font = "#000000";
	public static String lightskyblue4 = "#607b8b";
	public static String lightskyblue4Font = "#000000";
	public static String lightslateblue = "#8470ff";
	public static String lightslateblueFont = "#000000";
	public static String lightslategray = "#778899";
	public static String lightslategrayFont = "#000000";

	public static String lightslategrey = "#778899";
	public static String lightslategreyFont = "#000000";
	public static String lightsteelblue = "#b0c4de";
	public static String lightsteelblueFont = "#000000";
	public static String lightsteelblue1 = "#cae1ff";
	public static String lightsteelblue1Font = "#000000";
	public static String lightsteelblue2 = "#bcd2ee";
	public static String lightsteelblue2Font = "#000000";
	public static String lightsteelblue3 = "#a2b5cd";
	public static String lightsteelblue3Font = "#000000";

	public static String lightsteelblue4 = "#6e7b8b";
	public static String lightsteelblue4Font = "#000000";
	public static String lightyellow = "#ffffe0";
	public static String lightyellowFont = "#000000";
	public static String lightyellow1 = "#ffffe0";
	public static String lightyellow1Font = "#000000";
	public static String lightyellow2 = "#eeeed1";
	public static String lightyellow2Font = "#000000";
	public static String lightyellow3 = "#cdcdb4";
	public static String lightyellow3Font = "#000000";

	public static String lightyellow4 = "#8b8b7a";
	public static String lightyellow4Font = "#000000";
	public static String limegreen = "#32cd32";
	public static String limegreenFont = "#000000";
	public static String linen = "#faf0e6";
	public static String linenFont = "#000000";
	public static String magenta = "#ff00ff";
	public static String magentaFont = "#000000";
	public static String magenta1 = "#ff00ff";
	public static String magenta1Font = "#000000";

	public static String magenta2 = "#ee00ee";
	public static String magenta2Font = "#000000";
	public static String magenta3 = "#cd00cd";
	public static String magenta3Font = "#000000";
	public static String magenta4 = "#8b008b";
	public static String magenta4Font = "#000000";
	public static String maroon = "#b03060";
	public static String maroonFont = "#000000";
	public static String maroon1 = "#ff34b3";
	public static String maroon1Font = "#000000";

	public static String maroon2 = "#ee30a7";
	public static String maroon2Font = "#000000";
	public static String maroon3 = "#cd2990";
	public static String maroon3Font = "#000000";
	public static String maroon4 = "#8b1c62";
	public static String maroon4Font = "#000000";
	public static String mediumaquamarine = "#66cdaa";
	public static String mediumaquamarineFont = "#000000";
	public static String mediumblue = "#0000cd";
	public static String mediumblueFont = "#000000";

	public static String mediumorchid = "#ba55d3";
	public static String mediumorchidFont = "#000000";
	public static String mediumorchid1 = "#e066ff";
	public static String mediumorchid1Font = "#000000";
	public static String mediumorchid2 = "#d15fee";
	public static String mediumorchid2Font = "#000000";
	public static String mediumorchid3 = "#b452cd";
	public static String mediumorchid3Font = "#000000";
	public static String mediumorchid4 = "#7a378b";
	public static String mediumorchid4Font = "#000000";

	public static String mediumpurple = "#9370db";
	public static String mediumpurpleFont = "#000000";
	public static String mediumpurple1 = "#ab82ff";
	public static String mediumpurple1Font = "#000000";
	public static String mediumpurple2 = "#9f79ee";
	public static String mediumpurple2Font = "#000000";
	public static String mediumpurple3 = "#8968cd";
	public static String mediumpurple3Font = "#000000";
	public static String mediumpurple4 = "#5d478b";
	public static String mediumpurple4Font = "#000000";

	public static String mediumseagreen = "#3cb371";
	public static String mediumseagreenFont = "#000000";
	public static String mediumslateblue = "#7b68ee";
	public static String mediumslateblueFont = "#000000";
	public static String mediumspringgreen = "#00fa9a";
	public static String mediumspringgreenFont = "#000000";
	public static String mediumturquoise = "#48d1cc";
	public static String mediumturquoiseFont = "#000000";
	public static String mediumvioletred = "#c71585";
	public static String mediumvioletredFont = "#000000";

	public static String midnightblue = "#191970";
	public static String midnightblueFont = "#FFFFFF";
	public static String mintcream = "#f5fffa";
	public static String mintcreamFont = "#000000";
	public static String mistyrose = "#ffe4e1";
	public static String mistyroseFont = "#000000";
	public static String mistyrose1 = "#ffe4e1";
	public static String mistyrose1Font = "#000000";
	public static String mistyrose2 = "#eed5d2";
	public static String mistyrose2Font = "#000000";

	public static String mistyrose3 = "#cdb7b5";
	public static String mistyrose3Font = "#000000";
	public static String mistyrose4 = "#8b7d7b";
	public static String mistyrose4Font = "#000000";
	public static String moccasin = "#ffe4b5";
	public static String moccasinFont = "#000000";
	public static String navajowhite = "#ffdead";
	public static String navajowhiteFont = "#000000";
	public static String navajowhite1 = "#ffdead";
	public static String navajowhite1Font = "#000000";

	public static String navajowhite2 = "#eecfa1";
	public static String navajowhite2Font = "#000000";
	public static String navajowhite3 = "#cdb38b";
	public static String navajowhite3Font = "#000000";
	public static String navajowhite4 = "#8b795e";
	public static String navajowhite4Font = "#000000";
	public static String navy = "#000080";
	public static String navyFont = "#FFFFFF";
	public static String navyblue = "#000080";
	public static String navyblueFont = "#FFFFFF";

	public static String oldlace = "#fdf5e6";
	public static String oldlaceFont = "#000000";
	public static String olivedrab = "#6b8e23";
	public static String olivedrabFont = "#000000";
	public static String olivedrab1 = "#c0ff3e";
	public static String olivedrab1Font = "#000000";
	public static String olivedrab2 = "#b3ee3a";
	public static String olivedrab2Font = "#000000";
	public static String olivedrab3 = "#9acd32";
	public static String olivedrab3Font = "#000000";

	public static String olivedrab4 = "#698b22";
	public static String olivedrab4Font = "#000000";
	public static String orange = "#ffa500";
	public static String orangeFont = "#000000";
	public static String orange1 = "#ffa500";
	public static String orange1Font = "#000000";
	public static String orange2 = "#ee9a00";
	public static String orange2Font = "#000000";
	public static String orange3 = "#cd8500";
	public static String orange3Font = "#000000";

	public static String orange4 = "#8b5a00";
	public static String orange4Font = "#000000";
	public static String orangered = "#ff4500";
	public static String orangeredFont = "#000000";
	public static String orangered1 = "#ff4500";
	public static String orangered1Font = "#000000";
	public static String orangered2 = "#ee4000";
	public static String orangered2Font = "#000000";
	public static String orangered3 = "#cd3700";
	public static String orangered3Font = "#000000";

	public static String orangered4 = "#8b2500";
	public static String orangered4Font = "#000000";
	public static String orchid = "#da70d6";
	public static String orchidFont = "#000000";
	public static String orchid1 = "#ff83fa";
	public static String orchid1Font = "#000000";
	public static String orchid2 = "#ee7ae9";
	public static String orchid2Font = "#000000";
	public static String orchid3 = "#cd69c9";
	public static String orchid3Font = "#000000";

	public static String orchid4 = "#8b4789";
	public static String orchid4Font = "#000000";
	public static String palegoldenrod = "#eee8aa";
	public static String palegoldenrodFont = "#000000";
	public static String palegreen = "#98fb98";
	public static String palegreenFont = "#000000";
	public static String palegreen1 = "#9aff9a";
	public static String palegreen1Font = "#000000";
	public static String palegreen2 = "#90ee90";
	public static String palegreen2Font = "#000000";

	public static String palegreen3 = "#7ccd7c";
	public static String palegreen3Font = "#000000";
	public static String palegreen4 = "#548b54";
	public static String palegreen4Font = "#000000";
	public static String paleturquoise = "#afeeee";
	public static String paleturquoiseFont = "#000000";
	public static String paleturquoise1 = "#bbffff";
	public static String paleturquoise1Font = "#000000";
	public static String paleturquoise2 = "#aeeeee";
	public static String paleturquoise2Font = "#000000";

	public static String paleturquoise3 = "#96cdcd";
	public static String paleturquoise3Font = "#000000";
	public static String paleturquoise4 = "#668b8b";
	public static String paleturquoise4Font = "#000000";
	public static String palevioletred = "#db7093";
	public static String palevioletredFont = "#000000";
	public static String palevioletred1 = "#ff82ab";
	public static String palevioletred1Font = "#000000";
	public static String palevioletred2 = "#ee799f";
	public static String palevioletred2Font = "#000000";

	public static String palevioletred3 = "#cd6889";
	public static String palevioletred3Font = "#000000";
	public static String palevioletred4 = "#8b475d";
	public static String palevioletred4Font = "#000000";
	public static String papayawhip = "#ffefd5";
	public static String papayawhipFont = "#000000";
	public static String peachpuff = "#ffdab9";
	public static String peachpuffFont = "#000000";
	public static String peachpuff1 = "#ffdab9";
	public static String peachpuff1Font = "#000000";

	public static String peachpuff2 = "#eecbad";
	public static String peachpuff2Font = "#000000";
	public static String peachpuff3 = "#cdaf95";
	public static String peachpuff3Font = "#000000";
	public static String peachpuff4 = "#8b7765";
	public static String peachpuff4Font = "#000000";
	public static String peru = "#cd853f";
	public static String peruFont = "#000000";
	public static String pink = "#ffc0cb";
	public static String pinkFont = "#000000";

	public static String pink1 = "#ffb5c5";
	public static String pink1Font = "#000000";
	public static String pink2 = "#eea9b8";
	public static String pink2Font = "#000000";
	public static String pink3 = "#cd919e";
	public static String pink3Font = "#000000";
	public static String pink4 = "#8b636c";
	public static String pink4Font = "#000000";
	public static String plum = "#dda0dd";
	public static String plumFont = "#000000";

	public static String plum1 = "#ffbbff";
	public static String plum1Font = "#000000";
	public static String plum2 = "#eeaeee";
	public static String plum2Font = "#000000";
	public static String plum3 = "#cd96cd";
	public static String plum3Font = "#000000";
	public static String plum4 = "#8b668b";
	public static String plum4Font = "#000000";
	public static String powderblue = "#b0e0e6";
	public static String powderblueFont = "#000000";

	public static String purple = "#a020f0";
	public static String purpleFont = "#000000";
	public static String purple1 = "#9b30ff";
	public static String purple1Font = "#000000";
	public static String purple2 = "#912cee";
	public static String purple2Font = "#000000";
	public static String purple3 = "#7d26cd";
	public static String purple3Font = "#000000";
	public static String purple4 = "#551a8b";
	public static String purple4Font = "#000000";

	public static String red = "#ff0000";
	public static String redFont = "#000000";
	public static String red1 = "#ff0000";
	public static String red1Font = "#000000";
	public static String red2 = "#ee0000";
	public static String red2Font = "#000000";
	public static String red3 = "#cd0000";
	public static String red3Font = "#000000";
	public static String red4 = "#8b0000";
	public static String red4Font = "#000000";

	public static String rosybrown = "#bc8f8f";
	public static String rosybrownFont = "#000000";
	public static String rosybrown1 = "#ffc1c1";
	public static String rosybrown1Font = "#000000";
	public static String rosybrown2 = "#eeb4b4";
	public static String rosybrown2Font = "#000000";
	public static String rosybrown3 = "#cd9b9b";
	public static String rosybrown3Font = "#000000";
	public static String rosybrown4 = "#8b6969";
	public static String rosybrown4Font = "#000000";

	public static String royalblue = "#4169e1";
	public static String royalblueFont = "#000000";
	public static String royalblue1 = "#4876ff";
	public static String royalblue1Font = "#000000";
	public static String royalblue2 = "#436eee";
	public static String royalblue2Font = "#000000";
	public static String royalblue3 = "#3a5fcd";
	public static String royalblue3Font = "#000000";
	public static String royalblue4 = "#27408b";
	public static String royalblue4Font = "#000000";

	public static String saddlebrown = "#8b4513";
	public static String saddlebrownFont = "#000000";
	public static String salmon = "#fa8072";
	public static String salmonFont = "#000000";
	public static String salmon1 = "#ff8c69";
	public static String salmon1Font = "#000000";
	public static String salmon2 = "#ee8262";
	public static String salmon2Font = "#000000";
	public static String salmon3 = "#cd7054";
	public static String salmon3Font = "#000000";

	public static String salmon4 = "#8b4c39";
	public static String salmon4Font = "#000000";
	public static String sandybrown = "#f4a460";
	public static String sandybrownFont = "#000000";
	public static String seagreen = "#2e8b57";
	public static String seagreenFont = "#000000";
	public static String seagreen1 = "#54ff9f";
	public static String seagreen1Font = "#000000";
	public static String seagreen2 = "#4eee94";
	public static String seagreen2Font = "#000000";

	public static String seagreen3 = "#43cd80";
	public static String seagreen3Font = "#000000";
	public static String seagreen4 = "#2e8b57";
	public static String seagreen4Font = "#000000";
	public static String seashell = "#fff5ee";
	public static String seashellFont = "#000000";
	public static String seashell1 = "#fff5ee";
	public static String seashell1Font = "#000000";
	public static String seashell2 = "#eee5de";
	public static String seashell2Font = "#000000";

	public static String seashell3 = "#cdc5bf";
	public static String seashell3Font = "#000000";
	public static String seashell4 = "#8b8682";
	public static String seashell4Font = "#000000";
	public static String sienna = "#a0522d";
	public static String siennaFont = "#000000";
	public static String sienna1 = "#ff8247";
	public static String sienna1Font = "#000000";
	public static String sienna2 = "#ee7942";
	public static String sienna2Font = "#000000";

	public static String sienna3 = "#cd6839";
	public static String sienna3Font = "#000000";
	public static String sienna4 = "#8b4726";
	public static String sienna4Font = "#000000";
	public static String skyblue = "#87ceeb";
	public static String skyblueFont = "#000000";
	public static String skyblue1 = "#87ceff";
	public static String skyblue1Font = "#000000";
	public static String skyblue2 = "#7ec0ee";
	public static String skyblue2Font = "#000000";

	public static String skyblue3 = "#6ca6cd";
	public static String skyblue3Font = "#000000";
	public static String skyblue4 = "#4a708b";
	public static String skyblue4Font = "#000000";
	public static String slateblue = "#6a5acd";
	public static String slateblueFont = "#000000";
	public static String slateblue1 = "#836fff";
	public static String slateblue1Font = "#000000";
	public static String slateblue2 = "#7a67ee";
	public static String slateblue2Font = "#000000";

	public static String slateblue3 = "#6959cd";
	public static String slateblue3Font = "#000000";
	public static String slateblue4 = "#473c8b";
	public static String slateblue4Font = "#000000";
	public static String slategray = "#708090";
	public static String slategrayFont = "#000000";
	public static String slategray1 = "#c6e2ff";
	public static String slategray1Font = "#000000";
	public static String slategray2 = "#b9d3ee";
	public static String slategray2Font = "#000000";

	public static String slategray3 = "#9fb6cd";
	public static String slategray3Font = "#000000";
	public static String slategray4 = "#6c7b8b";
	public static String slategray4Font = "#000000";
	public static String slategrey = "#708090";
	public static String slategreyFont = "#000000";
	public static String snow = "#fffafa";
	public static String snowFont = "#000000";
	public static String snow1 = "#fffafa";
	public static String snow1Font = "#000000";

	public static String snow2 = "#eee9e9";
	public static String snow2Font = "#000000";
	public static String snow3 = "#cdc9c9";
	public static String snow3Font = "#000000";
	public static String snow4 = "#8b8989";
	public static String snow4Font = "#000000";
	public static String springgreen = "#00ff7f";
	public static String springgreenFont = "#000000";
	public static String springgreen1 = "#00ff7f";
	public static String springgreen1Font = "#000000";

	public static String springgreen2 = "#00ee76";
	public static String springgreen2Font = "#000000";
	public static String springgreen3 = "#00cd66";
	public static String springgreen3Font = "#000000";
	public static String springgreen4 = "#008b45";
	public static String springgreen4Font = "#000000";
	public static String steelblue = "#4682b4";
	public static String steelblueFont = "#000000";
	public static String steelblue1 = "#63b8ff";
	public static String steelblue1Font = "#000000";

	public static String steelblue2 = "#5cacee";
	public static String steelblue2Font = "#000000";
	public static String steelblue3 = "#4f94cd";
	public static String steelblue3Font = "#000000";
	public static String steelblue4 = "#36648b";
	public static String steelblue4Font = "#000000";
	public static String tan = "#d2b48c";
	public static String tanFont = "#000000";
	public static String tan1 = "#ffa54f";
	public static String tan1Font = "#000000";

	public static String tan2 = "#ee9a49";
	public static String tan2Font = "#000000";
	public static String tan3 = "#cd853f";
	public static String tan3Font = "#000000";
	public static String tan4 = "#8b5a2b";
	public static String tan4Font = "#000000";
	public static String thistle = "#d8bfd8";
	public static String thistleFont = "#000000";
	public static String thistle1 = "#ffe1ff";
	public static String thistle1Font = "#000000";

	public static String thistle2 = "#eed2ee";
	public static String thistle2Font = "#000000";
	public static String thistle3 = "#cdb5cd";
	public static String thistle3Font = "#000000";
	public static String thistle4 = "#8b7b8b";
	public static String thistle4Font = "#000000";
	public static String tomato = "#ff6347";
	public static String tomatoFont = "#000000";
	public static String tomato1 = "#ff6347";
	public static String tomato1Font = "#000000";

	public static String tomato2 = "#ee5c42";
	public static String tomato2Font = "#000000";
	public static String tomato3 = "#cd4f39";
	public static String tomato3Font = "#000000";
	public static String tomato4 = "#8b3626";
	public static String tomato4Font = "#000000";
	public static String transparent = "#fffffe";
	public static String transparentFont = "#000000";
	public static String turquoise = "#40e0d0";
	public static String turquoiseFont = "#000000";

	public static String turquoise1 = "#00f5ff";
	public static String turquoise1Font = "#000000";
	public static String turquoise2 = "#00e5ee";
	public static String turquoise2Font = "#000000";
	public static String turquoise3 = "#00c5cd";
	public static String turquoise3Font = "#000000";
	public static String turquoise4 = "#00868b";
	public static String turquoise4Font = "#000000";
	public static String violet = "#ee82ee";
	public static String violetFont = "#000000";

	public static String violetred = "#d02090";
	public static String violetredFont = "#000000";
	public static String violetred1 = "#ff3e96";
	public static String violetred1Font = "#000000";
	public static String violetred2 = "#ee3a8c";
	public static String violetred2Font = "#000000";
	public static String violetred3 = "#cd3278";
	public static String violetred3Font = "#000000";
	public static String violetred4 = "#8b2252";
	public static String violetred4Font = "#000000";

	public static String wheat = "#f5deb3";
	public static String wheatFont = "#000000";
	public static String wheat1 = "#ffe7ba";
	public static String wheat1Font = "#000000";
	public static String wheat2 = "#eed8ae";
	public static String wheat2Font = "#000000";
	public static String wheat3 = "#cdba96";
	public static String wheat3Font = "#000000";
	public static String wheat4 = "#8b7e66";
	public static String wheat4Font = "#000000";

	public static String white = "#ffffff";
	public static String whiteFont = "#000000";
	public static String whitesmoke = "#f5f5f5";
	public static String whitesmokeFont = "#000000";
	public static String yellow = "#ffff00";
	public static String yellowFont = "#000000";
	public static String yellow1 = "#ffff00";
	public static String yellow1Font = "#000000";
	public static String yellow2 = "#eeee00";
	public static String yellow2Font = "#000000";

	public static String yellow3 = "#cdcd00";
	public static String yellow3Font = "#000000";
	public static String yellow4 = "#8b8b00";
	public static String yellow4Font = "#000000";
	public static String yellowgreen = "#9acd32";
	public static String yellowgreenFont = "#000000";

	public static String[] accent3 = new String[] { "#7fc97f", "#beaed4", "#fdc086" };
	public static String[] accent4 = new String[] { "#7fc97f", "#beaed4", "#fdc086",
			"#ffff99" };
	public static String[] accent5 = new String[] { "#7fc97f", "#beaed4", "#fdc086",
			"#ffff99", "#386cb0" };
	public static String[] accent6 = new String[] { "#7fc97f", "#beaed4", "#fdc086",
			"#ffff99", "#386cb0", "#f0027f" };
	public static String[] accent7 = new String[] { "#7fc97f", "#beaed4", "#fdc086",
			"#ffff99", "#386cb0", "#f0027f", "#bf5b17" };
	public static String[] accent8 = new String[] { "#7fc97f", "#beaed4", "#fdc086",
			"#ffff99", "#386cb0", "#f0027f", "#bf5b17", "#666666" };
	public static String[] blues3 = new String[] { "#deebf7", "#9ecae1", "#3182bd" };
	public static String[] blues4 = new String[] { "#eff3ff", "#bdd7e7", "#6baed6",
			"#2171b5" };
	public static String[] blues5 = new String[] { "#eff3ff", "#bdd7e7", "#6baed6",
			"#3182bd", "#08519c" };
	public static String[] blues6 = new String[] { "#eff3ff", "#c6dbef", "#9ecae1",
			"#6baed6", "#3182bd", "#08519c" };
	public static String[] blues7 = new String[] { "#eff3ff", "#c6dbef", "#9ecae1",
			"#6baed6", "#4292c6", "#2171b5", "#084594" };
	public static String[] blues8 = new String[] { "#f7fbff", "#deebf7", "#c6dbef",
			"#9ecae1", "#6baed6", "#4292c6", "#2171b5", "#084594" };
	public static String[] blues9 = new String[] { "#f7fbff", "#deebf7", "#c6dbef",
			"#9ecae1", "#6baed6", "#4292c6", "#2171b5", "#08519c", "#08306b" };
	public static String[] brbg3 = new String[] { "#d8b365", "#f5f5f5", "#5ab4ac" };
	public static String[] brbg4 = new String[] { "#a6611a", "#dfc27d", "#80cdc1",
			"#018571" };
	public static String[] brbg5 = new String[] { "#a6611a", "#dfc27d", "#f5f5f5",
			"#80cdc1", "#018571" };
	public static String[] brbg6 = new String[] { "#8c510a", "#d8b365", "#f6e8c3",
			"#c7eae5", "#5ab4ac", "#01665e" };
	public static String[] brbg7 = new String[] { "#8c510a", "#d8b365", "#f6e8c3",
			"#f5f5f5", "#c7eae5", "#5ab4ac", "#01665e" };
	public static String[] brbg8 = new String[] { "#8c510a", "#bf812d", "#dfc27d",
			"#f6e8c3", "#c7eae5", "#80cdc1", "#35978f", "#01665e" };
	public static String[] brbg9 = new String[] { "#8c510a", "#bf812d", "#dfc27d",
			"#f6e8c3", "#f5f5f5", "#c7eae5", "#80cdc1", "#35978f", "#01665e" };
	public static String[] brbg10 = new String[] { "#543005", "#8c510a", "#bf812d",
			"#dfc27d", "#f6e8c3", "#c7eae5", "#80cdc1", "#35978f", "#01665e",
			"#003c30" };
	public static String[] brbg11 = new String[] { "#543005", "#8c510a", "#bf812d",
			"#dfc27d", "#f6e8c3", "#f5f5f5", "#c7eae5", "#80cdc1", "#35978f",
			"#01665e", "#003c30" };
	public static String[] bugn3 = new String[] { "#e5f5f9", "#99d8c9", "#2ca25f" };
	public static String[] bugn4 = new String[] { "#edf8fb", "#b2e2e2", "#66c2a4",
			"#238b45" };
	public static String[] bugn5 = new String[] { "#edf8fb", "#b2e2e2", "#66c2a4",
			"#2ca25f", "#006d2c" };
	public static String[] bugn6 = new String[] { "#edf8fb", "#ccece6", "#99d8c9",
			"#66c2a4", "#2ca25f", "#006d2c" };
	public static String[] bugn7 = new String[] { "#edf8fb", "#ccece6", "#99d8c9",
			"#66c2a4", "#41ae76", "#238b45", "#005824" };
	public static String[] bugn8 = new String[] { "#f7fcfd", "#e5f5f9", "#ccece6",
			"#99d8c9", "#66c2a4", "#41ae76", "#238b45", "#005824" };
	public static String[] bugn9 = new String[] { "#f7fcfd", "#e5f5f9", "#ccece6",
			"#99d8c9", "#66c2a4", "#41ae76", "#238b45", "#006d2c", "#00441b" };
	public static String[] bupu3 = new String[] { "#e0ecf4", "#9ebcda", "#8856a7" };
	public static String[] bupu4 = new String[] { "#edf8fb", "#b3cde3", "#8c96c6",
			"#88419d" };
	public static String[] bupu5 = new String[] { "#edf8fb", "#b3cde3", "#8c96c6",
			"#8856a7", "#810f7c" };
	public static String[] bupu6 = new String[] { "#edf8fb", "#bfd3e6", "#9ebcda",
			"#8c96c6", "#8856a7", "#810f7c" };
	public static String[] bupu7 = new String[] { "#edf8fb", "#bfd3e6", "#9ebcda",
			"#8c96c6", "#8c6bb1", "#88419d", "#6e016b" };
	public static String[] bupu8 = new String[] { "#f7fcfd", "#e0ecf4", "#bfd3e6",
			"#9ebcda", "#8c96c6", "#8c6bb1", "#88419d", "#6e016b" };
	public static String[] bupu9 = new String[] { "#f7fcfd", "#e0ecf4", "#bfd3e6",
			"#9ebcda", "#8c96c6", "#8c6bb1", "#88419d", "#810f7c", "#4d004b" };
	public static String[] dark23 = new String[] { "#1b9e77", "#d95f02", "#7570b3" };
	public static String[] dark24 = new String[] { "#1b9e77", "#d95f02", "#7570b3",
			"#e7298a" };
	public static String[] dark25 = new String[] { "#1b9e77", "#d95f02", "#7570b3",
			"#e7298a", "#66a61e" };
	public static String[] dark26 = new String[] { "#1b9e77", "#d95f02", "#7570b3",
			"#e7298a", "#66a61e", "#e6ab02" };
	public static String[] dark27 = new String[] { "#1b9e77", "#d95f02", "#7570b3",
			"#e7298a", "#66a61e", "#e6ab02", "#a6761d" };
	public static String[] dark28 = new String[] { "#1b9e77", "#d95f02", "#7570b3",
			"#e7298a", "#66a61e", "#e6ab02", "#a6761d", "#666666" };
	public static String[] gnbu3 = new String[] { "#e0f3db", "#a8ddb5", "#43a2ca" };
	public static String[] gnbu4 = new String[] { "#f0f9e8", "#bae4bc", "#7bccc4",
			"#2b8cbe" };
	public static String[] gnbu5 = new String[] { "#f0f9e8", "#bae4bc", "#7bccc4",
			"#43a2ca", "#0868ac" };
	public static String[] gnbu6 = new String[] { "#f0f9e8", "#ccebc5", "#a8ddb5",
			"#7bccc4", "#43a2ca", "#0868ac" };
	public static String[] gnbu7 = new String[] { "#f0f9e8", "#ccebc5", "#a8ddb5",
			"#7bccc4", "#4eb3d3", "#2b8cbe", "#08589e" };
	public static String[] gnbu8 = new String[] { "#f7fcf0", "#e0f3db", "#ccebc5",
			"#a8ddb5", "#7bccc4", "#4eb3d3", "#2b8cbe", "#08589e" };
	public static String[] gnbu9 = new String[] { "#f7fcf0", "#e0f3db", "#ccebc5",
			"#a8ddb5", "#7bccc4", "#4eb3d3", "#2b8cbe", "#0868ac", "#084081" };
	public static String[] greens3 = new String[] { "#e5f5e0", "#a1d99b", "#31a354" };
	public static String[] greens4 = new String[] { "#edf8e9", "#bae4b3", "#74c476",
			"#238b45" };
	public static String[] greens5 = new String[] { "#edf8e9", "#bae4b3", "#74c476",
			"#31a354", "#006d2c" };
	public static String[] greens6 = new String[] { "#edf8e9", "#c7e9c0", "#a1d99b",
			"#74c476", "#31a354", "#006d2c" };
	public static String[] greens7 = new String[] { "#edf8e9", "#c7e9c0", "#a1d99b",
			"#74c476", "#41ab5d", "#238b45", "#005a32" };
	public static String[] greens8 = new String[] { "#f7fcf5", "#e5f5e0", "#c7e9c0",
			"#a1d99b", "#74c476", "#41ab5d", "#238b45", "#005a32" };
	public static String[] greens9 = new String[] { "#f7fcf5", "#e5f5e0", "#c7e9c0",
			"#a1d99b", "#74c476", "#41ab5d", "#238b45", "#006d2c", "#00441b" };
	public static String[] greys3 = new String[] { "#f0f0f0", "#bdbdbd", "#636363" };
	public static String[] greys4 = new String[] { "#f7f7f7", "#cccccc", "#969696",
			"#525252" };
	public static String[] greys5 = new String[] { "#f7f7f7", "#cccccc", "#969696",
			"#636363", "#252525" };
	public static String[] greys6 = new String[] { "#f7f7f7", "#d9d9d9", "#bdbdbd",
			"#969696", "#636363", "#252525" };
	public static String[] greys7 = new String[] { "#f7f7f7", "#d9d9d9", "#bdbdbd",
			"#969696", "#737373", "#525252", "#252525" };
	public static String[] greys8 = new String[] { "#ffffff", "#f0f0f0", "#d9d9d9",
			"#bdbdbd", "#969696", "#737373", "#525252", "#252525" };
	public static String[] greys9 = new String[] { "#ffffff", "#f0f0f0", "#d9d9d9",
			"#bdbdbd", "#969696", "#737373", "#525252", "#252525", "#000000" };
	public static String[] oranges3 = new String[] { "#fee6ce", "#fdae6b", "#e6550d" };
	public static String[] oranges4 = new String[] { "#feedde", "#fdbe85", "#fd8d3c",
			"#d94701" };
	public static String[] oranges5 = new String[] { "#feedde", "#fdbe85", "#fd8d3c",
			"#e6550d", "#a63603" };
	public static String[] oranges6 = new String[] { "#feedde", "#fdd0a2", "#fdae6b",
			"#fd8d3c", "#e6550d", "#a63603" };
	public static String[] oranges7 = new String[] { "#feedde", "#fdd0a2", "#fdae6b",
			"#fd8d3c", "#f16913", "#d94801", "#8c2d04" };
	public static String[] oranges8 = new String[] { "#fff5eb", "#fee6ce", "#fdd0a2",
			"#fdae6b", "#fd8d3c", "#f16913", "#d94801", "#8c2d04" };
	public static String[] oranges9 = new String[] { "#fff5eb", "#fee6ce", "#fdd0a2",
			"#fdae6b", "#fd8d3c", "#f16913", "#d94801", "#a63603", "#7f2704" };
	public static String[] orrd3 = new String[] { "#fee8c8", "#fdbb84", "#e34a33" };
	public static String[] orrd4 = new String[] { "#fef0d9", "#fdcc8a", "#fc8d59",
			"#d7301f" };
	public static String[] orrd5 = new String[] { "#fef0d9", "#fdcc8a", "#fc8d59",
			"#e34a33", "#b30000" };
	public static String[] orrd6 = new String[] { "#fef0d9", "#fdd49e", "#fdbb84",
			"#fc8d59", "#e34a33", "#b30000" };
	public static String[] orrd7 = new String[] { "#fef0d9", "#fdd49e", "#fdbb84",
			"#fc8d59", "#ef6548", "#d7301f", "#990000" };
	public static String[] orrd8 = new String[] { "#fff7ec", "#fee8c8", "#fdd49e",
			"#fdbb84", "#fc8d59", "#ef6548", "#d7301f", "#990000" };
	public static String[] orrd9 = new String[] { "#fff7ec", "#fee8c8", "#fdd49e",
			"#fdbb84", "#fc8d59", "#ef6548", "#d7301f", "#b30000", "#7f0000" };
	public static String[] paired3 = new String[] { "#a6cee3", "#1f78b4", "#b2df8a" };
	public static String[] paired4 = new String[] { "#a6cee3", "#1f78b4", "#b2df8a",
			"#33a02c" };
	public static String[] paired5 = new String[] { "#a6cee3", "#1f78b4", "#b2df8a",
			"#33a02c", "#fb9a99" };
	public static String[] paired6 = new String[] { "#a6cee3", "#1f78b4", "#b2df8a",
			"#33a02c", "#fb9a99", "#e31a1c" };
	public static String[] paired7 = new String[] { "#a6cee3", "#1f78b4", "#b2df8a",
			"#33a02c", "#fb9a99", "#e31a1c", "#fdbf6f" };
	public static String[] paired8 = new String[] { "#a6cee3", "#1f78b4", "#b2df8a",
			"#33a02c", "#fb9a99", "#e31a1c", "#fdbf6f", "#ff7f00" };
	public static String[] paired9 = new String[] { "#a6cee3", "#1f78b4", "#b2df8a",
			"#33a02c", "#fb9a99", "#e31a1c", "#fdbf6f", "#ff7f00", "#cab2d6" };
	public static String[] paired10 = new String[] { "#a6cee3", "#1f78b4", "#b2df8a",
			"#33a02c", "#fb9a99", "#e31a1c", "#fdbf6f", "#ff7f00", "#cab2d6",
			"#6a3d9a" };
	public static String[] paired11 = new String[] { "#a6cee3", "#1f78b4", "#b2df8a",
			"#33a02c", "#fb9a99", "#e31a1c", "#fdbf6f", "#ff7f00", "#cab2d6",
			"#6a3d9a", "#ffff99" };
	public static String[] paired12 = new String[] { "#a6cee3", "#1f78b4", "#b2df8a",
			"#33a02c", "#fb9a99", "#e31a1c", "#fdbf6f", "#ff7f00", "#cab2d6",
			"#6a3d9a", "#ffff99", "#b15928" };
	public static String[] pastel13 = new String[] { "#fbb4ae", "#b3cde3", "#ccebc5" };
	public static String[] pastel14 = new String[] { "#fbb4ae", "#b3cde3", "#ccebc5",
			"#decbe4" };
	public static String[] pastel15 = new String[] { "#fbb4ae", "#b3cde3", "#ccebc5",
			"#decbe4", "#fed9a6" };
	public static String[] pastel16 = new String[] { "#fbb4ae", "#b3cde3", "#ccebc5",
			"#decbe4", "#fed9a6", "#ffffcc" };
	public static String[] pastel17 = new String[] { "#fbb4ae", "#b3cde3", "#ccebc5",
			"#decbe4", "#fed9a6", "#ffffcc", "#e5d8bd" };
	public static String[] pastel18 = new String[] { "#fbb4ae", "#b3cde3", "#ccebc5",
			"#decbe4", "#fed9a6", "#ffffcc", "#e5d8bd", "#fddaec" };
	public static String[] pastel19 = new String[] { "#fbb4ae", "#b3cde3", "#ccebc5",
			"#decbe4", "#fed9a6", "#ffffcc", "#e5d8bd", "#fddaec", "#f2f2f2" };
	public static String[] pastel23 = new String[] { "#b3e2cd", "#fdcdac", "#cbd5e8" };
	public static String[] pastel24 = new String[] { "#b3e2cd", "#fdcdac", "#cbd5e8",
			"#f4cae4" };
	public static String[] pastel25 = new String[] { "#b3e2cd", "#fdcdac", "#cbd5e8",
			"#f4cae4", "#e6f5c9" };
	public static String[] pastel26 = new String[] { "#b3e2cd", "#fdcdac", "#cbd5e8",
			"#f4cae4", "#e6f5c9", "#fff2ae" };
	public static String[] pastel27 = new String[] { "#b3e2cd", "#fdcdac", "#cbd5e8",
			"#f4cae4", "#e6f5c9", "#fff2ae", "#f1e2cc" };
	public static String[] pastel28 = new String[] { "#b3e2cd", "#fdcdac", "#cbd5e8",
			"#f4cae4", "#e6f5c9", "#fff2ae", "#f1e2cc", "#cccccc" };
	public static String[] piyg3 = new String[] { "#e9a3c9", "#f7f7f7", "#a1d76a" };
	public static String[] piyg4 = new String[] { "#d01c8b", "#f1b6da", "#b8e186",
			"#4dac26" };
	public static String[] piyg5 = new String[] { "#d01c8b", "#f1b6da", "#f7f7f7",
			"#b8e186", "#4dac26" };
	public static String[] piyg6 = new String[] { "#c51b7d", "#e9a3c9", "#fde0ef",
			"#e6f5d0", "#a1d76a", "#4d9221" };
	public static String[] piyg7 = new String[] { "#c51b7d", "#e9a3c9", "#fde0ef",
			"#f7f7f7", "#e6f5d0", "#a1d76a", "#4d9221" };
	public static String[] piyg8 = new String[] { "#c51b7d", "#de77ae", "#f1b6da",
			"#fde0ef", "#e6f5d0", "#b8e186", "#7fbc41", "#4d9221" };
	public static String[] piyg9 = new String[] { "#c51b7d", "#de77ae", "#f1b6da",
			"#fde0ef", "#f7f7f7", "#e6f5d0", "#b8e186", "#7fbc41", "#4d9221" };
	public static String[] piyg10 = new String[] { "#8e0152", "#c51b7d", "#de77ae",
			"#f1b6da", "#fde0ef", "#e6f5d0", "#b8e186", "#7fbc41", "#4d9221",
			"#276419" };
	public static String[] piyg11 = new String[] { "#8e0152", "#c51b7d", "#de77ae",
			"#f1b6da", "#fde0ef", "#f7f7f7", "#e6f5d0", "#b8e186", "#7fbc41",
			"#4d9221", "#276419" };
	public static String[] prgn3 = new String[] { "#af8dc3", "#f7f7f7", "#7fbf7b" };
	public static String[] prgn4 = new String[] { "#7b3294", "#c2a5cf", "#a6dba0",
			"#008837" };
	public static String[] prgn5 = new String[] { "#7b3294", "#c2a5cf", "#f7f7f7",
			"#a6dba0", "#008837" };
	public static String[] prgn6 = new String[] { "#762a83", "#af8dc3", "#e7d4e8",
			"#d9f0d3", "#7fbf7b", "#1b7837" };
	public static String[] prgn7 = new String[] { "#762a83", "#af8dc3", "#e7d4e8",
			"#f7f7f7", "#d9f0d3", "#7fbf7b", "#1b7837" };
	public static String[] prgn8 = new String[] { "#762a83", "#9970ab", "#c2a5cf",
			"#e7d4e8", "#d9f0d3", "#a6dba0", "#5aae61", "#1b7837" };
	public static String[] prgn9 = new String[] { "#762a83", "#9970ab", "#c2a5cf",
			"#e7d4e8", "#f7f7f7", "#d9f0d3", "#a6dba0", "#5aae61", "#1b7837" };
	public static String[] prgn10 = new String[] { "#40004b", "#762a83", "#9970ab",
			"#c2a5cf", "#e7d4e8", "#d9f0d3", "#a6dba0", "#5aae61", "#1b7837",
			"#00441b" };
	public static String[] prgn11 = new String[] { "#40004b", "#762a83", "#9970ab",
			"#c2a5cf", "#e7d4e8", "#f7f7f7", "#d9f0d3", "#a6dba0", "#5aae61",
			"#1b7837", "#00441b" };
	public static String[] pubu3 = new String[] { "#ece7f2", "#a6bddb", "#2b8cbe" };
	public static String[] pubu4 = new String[] { "#f1eef6", "#bdc9e1", "#74a9cf",
			"#0570b0" };
	public static String[] pubu5 = new String[] { "#f1eef6", "#bdc9e1", "#74a9cf",
			"#2b8cbe", "#045a8d" };
	public static String[] pubu6 = new String[] { "#f1eef6", "#d0d1e6", "#a6bddb",
			"#74a9cf", "#2b8cbe", "#045a8d" };
	public static String[] pubu7 = new String[] { "#f1eef6", "#d0d1e6", "#a6bddb",
			"#74a9cf", "#3690c0", "#0570b0", "#034e7b" };
	public static String[] pubu8 = new String[] { "#fff7fb", "#ece7f2", "#d0d1e6",
			"#a6bddb", "#74a9cf", "#3690c0", "#0570b0", "#034e7b" };
	public static String[] pubu9 = new String[] { "#fff7fb", "#ece7f2", "#d0d1e6",
			"#a6bddb", "#74a9cf", "#3690c0", "#0570b0", "#045a8d", "#023858" };
	public static String[] pubugn3 = new String[] { "#ece2f0", "#a6bddb", "#1c9099" };
	public static String[] pubugn4 = new String[] { "#f6eff7", "#bdc9e1", "#67a9cf",
			"#02818a" };
	public static String[] pubugn5 = new String[] { "#f6eff7", "#bdc9e1", "#67a9cf",
			"#1c9099", "#016c59" };
	public static String[] pubugn6 = new String[] { "#f6eff7", "#d0d1e6", "#a6bddb",
			"#67a9cf", "#1c9099", "#016c59" };
	public static String[] pubugn7 = new String[] { "#f6eff7", "#d0d1e6", "#a6bddb",
			"#67a9cf", "#3690c0", "#02818a", "#016450" };
	public static String[] pubugn8 = new String[] { "#fff7fb", "#ece2f0", "#d0d1e6",
			"#a6bddb", "#67a9cf", "#3690c0", "#02818a", "#016450" };
	public static String[] pubugn9 = new String[] { "#fff7fb", "#ece2f0", "#d0d1e6",
			"#a6bddb", "#67a9cf", "#3690c0", "#02818a", "#016c59", "#014636" };
	public static String[] puor3 = new String[] { "#f1a340", "#f7f7f7", "#998ec3" };
	public static String[] puor4 = new String[] { "#e66101", "#fdb863", "#b2abd2",
			"#5e3c99" };
	public static String[] puor5 = new String[] { "#e66101", "#fdb863", "#f7f7f7",
			"#b2abd2", "#5e3c99" };
	public static String[] puor6 = new String[] { "#b35806", "#f1a340", "#fee0b6",
			"#d8daeb", "#998ec3", "#542788" };
	public static String[] puor7 = new String[] { "#b35806", "#f1a340", "#fee0b6",
			"#f7f7f7", "#d8daeb", "#998ec3", "#542788" };
	public static String[] puor8 = new String[] { "#b35806", "#e08214", "#fdb863",
			"#fee0b6", "#d8daeb", "#b2abd2", "#8073ac", "#542788" };
	public static String[] puor9 = new String[] { "#b35806", "#e08214", "#fdb863",
			"#fee0b6", "#f7f7f7", "#d8daeb", "#b2abd2", "#8073ac", "#542788" };
	public static String[] purd3 = new String[] { "#e7e1ef", "#c994c7", "#dd1c77" };
	public static String[] purd4 = new String[] { "#f1eef6", "#d7b5d8", "#df65b0",
			"#ce1256" };
	public static String[] purd5 = new String[] { "#f1eef6", "#d7b5d8", "#df65b0",
			"#dd1c77", "#980043" };
	public static String[] purd6 = new String[] { "#f1eef6", "#d4b9da", "#c994c7",
			"#df65b0", "#dd1c77", "#980043" };
	public static String[] purd7 = new String[] { "#f1eef6", "#d4b9da", "#c994c7",
			"#df65b0", "#e7298a", "#ce1256", "#91003f" };
	public static String[] purd8 = new String[] { "#f7f4f9", "#e7e1ef", "#d4b9da",
			"#c994c7", "#df65b0", "#e7298a", "#ce1256", "#91003f" };
	public static String[] purd9 = new String[] { "#f7f4f9", "#e7e1ef", "#d4b9da",
			"#c994c7", "#df65b0", "#e7298a", "#ce1256", "#980043", "#67001f" };
	public static String[] puor10 = new String[] { "#7f3b08", "#b35806", "#e08214",
			"#fdb863", "#fee0b6", "#d8daeb", "#b2abd2", "#8073ac", "#542788",
			"#2d004b" };
	public static String[] puor11 = new String[] { "#7f3b08", "#b35806", "#e08214",
			"#fdb863", "#fee0b6", "#f7f7f7", "#d8daeb", "#b2abd2", "#8073ac",
			"#542788", "#2d004b" };
	public static String[] purples3 = new String[] { "#efedf5", "#bcbddc", "#756bb1" };
	public static String[] purples4 = new String[] { "#f2f0f7", "#cbc9e2", "#9e9ac8",
			"#6a51a3" };
	public static String[] purples5 = new String[] { "#f2f0f7", "#cbc9e2", "#9e9ac8",
			"#756bb1", "#54278f" };
	public static String[] purples6 = new String[] { "#f2f0f7", "#dadaeb", "#bcbddc",
			"#9e9ac8", "#756bb1", "#54278f" };
	public static String[] purples7 = new String[] { "#f2f0f7", "#dadaeb", "#bcbddc",
			"#9e9ac8", "#807dba", "#6a51a3", "#4a1486" };
	public static String[] purples8 = new String[] { "#fcfbfd", "#efedf5", "#dadaeb",
			"#bcbddc", "#9e9ac8", "#807dba", "#6a51a3", "#4a1486" };
	public static String[] purples9 = new String[] { "#fcfbfd", "#efedf5", "#dadaeb",
			"#bcbddc", "#9e9ac8", "#807dba", "#6a51a3", "#54278f", "#3f007d" };
	public static String[] rdbu10 = new String[] { "#67001f", "#b2182b", "#d6604d",
			"#f4a582", "#fddbc7", "#d1e5f0", "#92c5de", "#4393c3", "#2166ac",
			"#053061" };
	public static String[] rdbu11 = new String[] { "#67001f", "#b2182b", "#d6604d",
			"#f4a582", "#fddbc7", "#f7f7f7", "#d1e5f0", "#92c5de", "#4393c3",
			"#2166ac", "#053061" };
	public static String[] rdbu3 = new String[] { "#ef8a62", "#f7f7f7", "#67a9cf" };
	public static String[] rdbu4 = new String[] { "#ca0020", "#f4a582", "#92c5de",
			"#0571b0" };
	public static String[] rdbu5 = new String[] { "#ca0020", "#f4a582", "#f7f7f7",
			"#92c5de", "#0571b0" };
	public static String[] rdbu6 = new String[] { "#b2182b", "#ef8a62", "#fddbc7",
			"#d1e5f0", "#67a9cf", "#2166ac" };
	public static String[] rdbu7 = new String[] { "#b2182b", "#ef8a62", "#fddbc7",
			"#f7f7f7", "#d1e5f0", "#67a9cf", "#2166ac" };
	public static String[] rdbu8 = new String[] { "#b2182b", "#d6604d", "#f4a582",
			"#fddbc7", "#d1e5f0", "#92c5de", "#4393c3", "#2166ac" };
	public static String[] rdbu9 = new String[] { "#b2182b", "#d6604d", "#f4a582",
			"#fddbc7", "#f7f7f7", "#d1e5f0", "#92c5de", "#4393c3", "#2166ac" };
	public static String[] rdgy3 = new String[] { "#ef8a62", "#ffffff", "#999999" };
	public static String[] rdgy4 = new String[] { "#ca0020", "#f4a582", "#bababa",
			"#404040" };
	public static String[] rdgy5 = new String[] { "#ca0020", "#f4a582", "#ffffff",
			"#bababa", "#404040" };
	public static String[] rdgy6 = new String[] { "#b2182b", "#ef8a62", "#fddbc7",
			"#e0e0e0", "#999999", "#4d4d4d" };
	public static String[] rdgy7 = new String[] { "#b2182b", "#ef8a62", "#fddbc7",
			"#ffffff", "#e0e0e0", "#999999", "#4d4d4d" };
	public static String[] rdgy8 = new String[] { "#b2182b", "#d6604d", "#f4a582",
			"#fddbc7", "#e0e0e0", "#bababa", "#878787", "#4d4d4d" };
	public static String[] rdgy9 = new String[] { "#b2182b", "#d6604d", "#f4a582",
			"#fddbc7", "#ffffff", "#e0e0e0", "#bababa", "#878787", "#4d4d4d" };
	public static String[] rdpu3 = new String[] { "#fde0dd", "#fa9fb5", "#c51b8a" };
	public static String[] rdpu4 = new String[] { "#feebe2", "#fbb4b9", "#f768a1",
			"#ae017e" };
	public static String[] rdpu5 = new String[] { "#feebe2", "#fbb4b9", "#f768a1",
			"#c51b8a", "#7a0177" };
	public static String[] rdpu6 = new String[] { "#feebe2", "#fcc5c0", "#fa9fb5",
			"#f768a1", "#c51b8a", "#7a0177" };
	public static String[] rdpu7 = new String[] { "#feebe2", "#fcc5c0", "#fa9fb5",
			"#f768a1", "#dd3497", "#ae017e", "#7a0177" };
	public static String[] rdpu8 = new String[] { "#fff7f3", "#fde0dd", "#fcc5c0",
			"#fa9fb5", "#f768a1", "#dd3497", "#ae017e", "#7a0177" };
	public static String[] rdpu9 = new String[] { "#fff7f3", "#fde0dd", "#fcc5c0",
			"#fa9fb5", "#f768a1", "#dd3497", "#ae017e", "#7a0177", "#49006a" };
	public static String[] rdgy10 = new String[] { "#67001f", "#b2182b", "#d6604d",
			"#f4a582", "#fddbc7", "#e0e0e0", "#bababa", "#878787", "#4d4d4d",
			"#1a1a1a" };
	public static String[] rdgy11 = new String[] { "#67001f", "#b2182b", "#d6604d",
			"#f4a582", "#fddbc7", "#ffffff", "#e0e0e0", "#bababa", "#878787",
			"#4d4d4d", "#1a1a1a" };
	public static String[] rdylbu3 = new String[] { "#fc8d59", "#ffffbf", "#91bfdb" };
	public static String[] rdylbu4 = new String[] { "#d7191c", "#fdae61", "#abd9e9",
			"#2c7bb6" };
	public static String[] rdylbu5 = new String[] { "#d7191c", "#fdae61", "#ffffbf",
			"#abd9e9", "#2c7bb6" };
	public static String[] rdylbu6 = new String[] { "#d73027", "#fc8d59", "#fee090",
			"#e0f3f8", "#91bfdb", "#4575b4" };
	public static String[] rdylbu7 = new String[] { "#d73027", "#fc8d59", "#fee090",
			"#ffffbf", "#e0f3f8", "#91bfdb", "#4575b4" };
	public static String[] rdylbu8 = new String[] { "#d73027", "#f46d43", "#fdae61",
			"#fee090", "#e0f3f8", "#abd9e9", "#74add1", "#4575b4" };
	public static String[] rdylbu9 = new String[] { "#d73027", "#f46d43", "#fdae61",
			"#fee090", "#ffffbf", "#e0f3f8", "#abd9e9", "#74add1", "#4575b4" };
	public static String[] rdylbu10 = new String[] { "#a50026", "#d73027", "#f46d43",
			"#fdae61", "#fee090", "#e0f3f8", "#abd9e9", "#74add1", "#4575b4",
			"#313695" };
	public static String[] rdylbu11 = new String[] { "#a50026", "#d73027", "#f46d43",
			"#fdae61", "#fee090", "#ffffbf", "#e0f3f8", "#abd9e9", "#74add1",
			"#4575b4", "#313695" };
	public static String[] rdylgn3 = new String[] { "#fc8d59", "#ffffbf", "#91cf60" };
	public static String[] rdylgn4 = new String[] { "#d7191c", "#fdae61", "#a6d96a",
			"#1a9641" };
	public static String[] rdylgn5 = new String[] { "#d7191c", "#fdae61", "#ffffbf",
			"#a6d96a", "#1a9641" };
	public static String[] rdylgn6 = new String[] { "#d73027", "#fc8d59", "#fee08b",
			"#d9ef8b", "#91cf60", "#1a9850" };
	public static String[] rdylgn7 = new String[] { "#d73027", "#fc8d59", "#fee08b",
			"#ffffbf", "#d9ef8b", "#91cf60", "#1a9850" };
	public static String[] rdylgn8 = new String[] { "#d73027", "#f46d43", "#fdae61",
			"#fee08b", "#d9ef8b", "#a6d96a", "#66bd63", "#1a9850" };
	public static String[] rdylgn9 = new String[] { "#d73027", "#f46d43", "#fdae61",
			"#fee08b", "#ffffbf", "#d9ef8b", "#a6d96a", "#66bd63", "#1a9850" };
	public static String[] rdylgn10 = new String[] { "#a50026", "#d73027", "#f46d43",
			"#fdae61", "#fee08b", "#d9ef8b", "#a6d96a", "#66bd63", "#1a9850",
			"#006837" };
	public static String[] rdylgn11 = new String[] { "#a50026", "#d73027", "#f46d43",
			"#fdae61", "#fee08b", "#ffffbf", "#d9ef8b", "#a6d96a", "#66bd63",
			"#1a9850", "#006837" };
	public static String[] reds3 = new String[] { "#fee0d2", "#fc9272", "#de2d26" };
	public static String[] reds4 = new String[] { "#fee5d9", "#fcae91", "#fb6a4a",
			"#cb181d" };
	public static String[] reds5 = new String[] { "#fee5d9", "#fcae91", "#fb6a4a",
			"#de2d26", "#a50f15" };
	public static String[] reds6 = new String[] { "#fee5d9", "#fcbba1", "#fc9272",
			"#fb6a4a", "#de2d26", "#a50f15" };
	public static String[] reds7 = new String[] { "#fee5d9", "#fcbba1", "#fc9272",
			"#fb6a4a", "#ef3b2c", "#cb181d", "#99000d" };
	public static String[] reds8 = new String[] { "#fff5f0", "#fee0d2", "#fcbba1",
			"#fc9272", "#fb6a4a", "#ef3b2c", "#cb181d", "#99000d" };
	public static String[] reds9 = new String[] { "#fff5f0", "#fee0d2", "#fcbba1",
			"#fc9272", "#fb6a4a", "#ef3b2c", "#cb181d", "#a50f15", "#67000d" };
	public static String[] set13 = new String[] { "#e41a1c", "#377eb8", "#4daf4a" };
	public static String[] set14 = new String[] { "#e41a1c", "#377eb8", "#4daf4a",
			"#984ea3" };
	public static String[] set15 = new String[] { "#e41a1c", "#377eb8", "#4daf4a",
			"#984ea3", "#ff7f00" };
	public static String[] set16 = new String[] { "#e41a1c", "#377eb8", "#4daf4a",
			"#984ea3", "#ff7f00", "#ffff33" };
	public static String[] set17 = new String[] { "#e41a1c", "#377eb8", "#4daf4a",
			"#984ea3", "#ff7f00", "#ffff33", "#a65628" };
	public static String[] set18 = new String[] { "#e41a1c", "#377eb8", "#4daf4a",
			"#984ea3", "#ff7f00", "#ffff33", "#a65628", "#f781bf" };
	public static String[] set19 = new String[] { "#e41a1c", "#377eb8", "#4daf4a",
			"#984ea3", "#ff7f00", "#ffff33", "#a65628", "#f781bf", "#999999" };
	public static String[] set23 = new String[] { "#66c2a5", "#fc8d62", "#8da0cb" };
	public static String[] set24 = new String[] { "#66c2a5", "#fc8d62", "#8da0cb",
			"#e78ac3" };
	public static String[] set25 = new String[] { "#66c2a5", "#fc8d62", "#8da0cb",
			"#e78ac3", "#a6d854" };
	public static String[] set26 = new String[] { "#66c2a5", "#fc8d62", "#8da0cb",
			"#e78ac3", "#a6d854", "#ffd92f" };
	public static String[] set27 = new String[] { "#66c2a5", "#fc8d62", "#8da0cb",
			"#e78ac3", "#a6d854", "#ffd92f", "#e5c494" };
	public static String[] set28 = new String[] { "#66c2a5", "#fc8d62", "#8da0cb",
			"#e78ac3", "#a6d854", "#ffd92f", "#e5c494", "#b3b3b3" };
	public static String[] set33 = new String[] { "#8dd3c7", "#ffffb3", "#bebada" };
	public static String[] set34 = new String[] { "#8dd3c7", "#ffffb3", "#bebada",
			"#fb8072" };
	public static String[] set35 = new String[] { "#8dd3c7", "#ffffb3", "#bebada",
			"#fb8072", "#80b1d3" };
	public static String[] set36 = new String[] { "#8dd3c7", "#ffffb3", "#bebada",
			"#fb8072", "#80b1d3", "#fdb462" };
	public static String[] set37 = new String[] { "#8dd3c7", "#ffffb3", "#bebada",
			"#fb8072", "#80b1d3", "#fdb462", "#b3de69" };
	public static String[] set38 = new String[] { "#8dd3c7", "#ffffb3", "#bebada",
			"#fb8072", "#80b1d3", "#fdb462", "#b3de69", "#fccde5" };
	public static String[] set39 = new String[] { "#8dd3c7", "#ffffb3", "#bebada",
			"#fb8072", "#80b1d3", "#fdb462", "#b3de69", "#fccde5", "#d9d9d9" };
	public static String[] set310 = new String[] { "#8dd3c7", "#ffffb3", "#bebada",
			"#fb8072", "#80b1d3", "#fdb462", "#b3de69", "#fccde5", "#d9d9d9",
			"#bc80bd" };
	public static String[] set311 = new String[] { "#8dd3c7", "#ffffb3", "#bebada",
			"#fb8072", "#80b1d3", "#fdb462", "#b3de69", "#fccde5", "#d9d9d9",
			"#bc80bd", "#ccebc5" };
	public static String[] set312 = new String[] { "#8dd3c7", "#ffffb3", "#bebada",
			"#fb8072", "#80b1d3", "#fdb462", "#b3de69", "#fccde5", "#d9d9d9",
			"#bc80bd", "#ccebc5", "#ffed6f" };
	public static String[] spectral3 = new String[] { "#fc8d59", "#ffffbf", "#99d594" };
	public static String[] spectral4 = new String[] { "#d7191c", "#fdae61", "#abdda4",
			"#2b83ba" };
	public static String[] spectral5 = new String[] { "#d7191c", "#fdae61", "#ffffbf",
			"#abdda4", "#2b83ba" };
	public static String[] spectral6 = new String[] { "#d53e4f", "#fc8d59", "#fee08b",
			"#e6f598", "#99d594", "#3288bd" };
	public static String[] spectral7 = new String[] { "#d53e4f", "#fc8d59", "#fee08b",
			"#ffffbf", "#e6f598", "#99d594", "#3288bd" };
	public static String[] spectral8 = new String[] { "#d53e4f", "#f46d43", "#fdae61",
			"#fee08b", "#e6f598", "#abdda4", "#66c2a5", "#3288bd" };
	public static String[] spectral9 = new String[] { "#d53e4f", "#f46d43", "#fdae61",
			"#fee08b", "#ffffbf", "#e6f598", "#abdda4", "#66c2a5", "#3288bd" };
	public static String[] spectral10 = new String[] { "#9e0142", "#d53e4f",
			"#f46d43", "#fdae61", "#fee08b", "#e6f598", "#abdda4", "#66c2a5",
			"#3288bd", "#5e4fa2" };
	public static String[] spectral11 = new String[] { "#9e0142", "#d53e4f",
			"#f46d43", "#fdae61", "#fee08b", "#ffffbf", "#e6f598", "#abdda4",
			"#66c2a5", "#3288bd", "#5e4fa2" };
	public static String[] ylgn3 = new String[] { "#f7fcb9", "#addd8e", "#31a354" };
	public static String[] ylgn4 = new String[] { "#ffffcc", "#c2e699", "#78c679",
			"#238443" };
	public static String[] ylgn5 = new String[] { "#ffffcc", "#c2e699", "#78c679",
			"#31a354", "#006837" };
	public static String[] ylgn6 = new String[] { "#ffffcc", "#d9f0a3", "#addd8e",
			"#78c679", "#31a354", "#006837" };
	public static String[] ylgn7 = new String[] { "#ffffcc", "#d9f0a3", "#addd8e",
			"#78c679", "#41ab5d", "#238443", "#005a32" };
	public static String[] ylgn8 = new String[] { "#ffffe5", "#f7fcb9", "#d9f0a3",
			"#addd8e", "#78c679", "#41ab5d", "#238443", "#005a32" };
	public static String[] ylgn9 = new String[] { "#ffffe5", "#f7fcb9", "#d9f0a3",
			"#addd8e", "#78c679", "#41ab5d", "#238443", "#006837", "#004529" };
	public static String[] ylgnbu3 = new String[] { "#edf8b1", "#7fcdbb", "#2c7fb8" };
	public static String[] ylgnbu4 = new String[] { "#ffffcc", "#a1dab4", "#41b6c4",
			"#225ea8" };
	public static String[] ylgnbu5 = new String[] { "#ffffcc", "#a1dab4", "#41b6c4",
			"#2c7fb8", "#253494" };
	public static String[] ylgnbu6 = new String[] { "#ffffcc", "#c7e9b4", "#7fcdbb",
			"#41b6c4", "#2c7fb8", "#253494" };
	public static String[] ylgnbu7 = new String[] { "#ffffcc", "#c7e9b4", "#7fcdbb",
			"#41b6c4", "#1d91c0", "#225ea8", "#0c2c84" };
	public static String[] ylgnbu8 = new String[] { "#ffffd9", "#edf8b1", "#c7e9b4",
			"#7fcdbb", "#41b6c4", "#1d91c0", "#225ea8", "#0c2c84" };
	public static String[] ylgnbu9 = new String[] { "#ffffd9", "#edf8b1", "#c7e9b4",
			"#7fcdbb", "#41b6c4", "#1d91c0", "#225ea8", "#253494", "#081d58" };
	public static String[] ylorbr3 = new String[] { "#fff7bc", "#fec44f", "#d95f0e" };
	public static String[] ylorbr4 = new String[] { "#ffffd4", "#fed98e", "#fe9929",
			"#cc4c02" };
	public static String[] ylorbr5 = new String[] { "#ffffd4", "#fed98e", "#fe9929",
			"#d95f0e", "#993404" };
	public static String[] ylorbr6 = new String[] { "#ffffd4", "#fee391", "#fec44f",
			"#fe9929", "#d95f0e", "#993404" };
	public static String[] ylorbr7 = new String[] { "#ffffd4", "#fee391", "#fec44f",
			"#fe9929", "#ec7014", "#cc4c02", "#8c2d04" };
	public static String[] ylorbr8 = new String[] { "#ffffe5", "#fff7bc", "#fee391",
			"#fec44f", "#fe9929", "#ec7014", "#cc4c02", "#8c2d04" };
	public static String[] ylorbr9 = new String[] { "#ffffe5", "#fff7bc", "#fee391",
			"#fec44f", "#fe9929", "#ec7014", "#cc4c02", "#993404", "#662506" };
	public static String[] ylorrd3 = new String[] { "#ffeda0", "#feb24c", "#f03b20" };
	public static String[] ylorrd4 = new String[] { "#ffffb2", "#fecc5c", "#fd8d3c",
			"#e31a1c" };
	public static String[] ylorrd5 = new String[] { "#ffffb2", "#fecc5c", "#fd8d3c",
			"#f03b20", "#bd0026" };
	public static String[] ylorrd6 = new String[] { "#ffffb2", "#fed976", "#feb24c",
			"#fd8d3c", "#f03b20", "#bd0026" };
	public static String[] ylorrd7 = new String[] { "#ffffb2", "#fed976", "#feb24c",
			"#fd8d3c", "#fc4e2a", "#e31a1c", "#b10026" };
	public static String[] ylorrd8 = new String[] { "#ffffcc", "#ffeda0", "#fed976",
			"#feb24c", "#fd8d3c", "#fc4e2a", "#e31a1c", "#b10026" };
	public static String[] ylorrd9 = new String[] { "#ffffcc", "#ffeda0", "#fed976",
			"#feb24c", "#fd8d3c", "#fc4e2a", "#e31a1c", "#bd0026", "#800026" };

	public static String[] accent3Font = new String[] { "#000000", "#000000",
			"#000000" };
	public static String[] accent4Font = new String[] { "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] accent5Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] accent6Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] accent7Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] accent8Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] blues3Font = new String[] { "#000000", "#000000", "#000000" };
	public static String[] blues4Font = new String[] { "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] blues5Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] blues6Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] blues7Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] blues8Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] blues9Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#FFFFFF" };
	public static String[] brbg3Font = new String[] { "#000000", "#000000", "#000000" };
	public static String[] brbg4Font = new String[] { "#000000", "#000000", "#000000",
			"#000000" };
	public static String[] brbg5Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] brbg6Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#FFFFFF" };
	public static String[] brbg7Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] brbg8Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] brbg9Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] brbg10Font = new String[] { "#FFFFFF", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#FFFFFF", "#FFFFFF" };
	public static String[] brbg11Font = new String[] { "#FFFFFF", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000", "#FFFFFF", "#FFFFFF" };
	public static String[] bugn3Font = new String[] { "#000000", "#000000", "#000000" };
	public static String[] bugn4Font = new String[] { "#000000", "#000000", "#000000",
			"#000000" };
	public static String[] bugn5Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#FFFFFF" };
	public static String[] bugn6Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#FFFFFF" };
	public static String[] bugn7Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] bugn8Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] bugn9Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#FFFFFF", "#FFFFFF" };
	public static String[] bupu3Font = new String[] { "#000000", "#000000", "#000000" };
	public static String[] bupu4Font = new String[] { "#000000", "#000000", "#000000",
			"#000000" };
	public static String[] bupu5Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#FFFFFF" };
	public static String[] bupu6Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#FFFFFF" };
	public static String[] bupu7Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] bupu8Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] bupu9Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#FFFFFF", "#FFFFFF" };
	public static String[] dark23Font = new String[] { "#000000", "#000000", "#000000" };
	public static String[] dark24Font = new String[] { "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] dark25Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] dark26Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] dark27Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] dark28Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] gnbu3Font = new String[] { "#000000", "#000000", "#000000" };
	public static String[] gnbu4Font = new String[] { "#000000", "#000000", "#000000",
			"#000000" };
	public static String[] gnbu5Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] gnbu6Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] gnbu7Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] gnbu8Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] gnbu9Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] greens3Font = new String[] { "#000000", "#000000",
			"#000000" };
	public static String[] greens4Font = new String[] { "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] greens5Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#FFFFFF" };
	public static String[] greens6Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] greens7Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] greens8Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] greens9Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#FFFFFF",
			"#FFFFFF" };
	public static String[] greys3Font = new String[] { "#000000", "#000000", "#FFFFFF" };
	public static String[] greys4Font = new String[] { "#000000", "#000000",
			"#000000", "#FFFFFF" };
	public static String[] greys5Font = new String[] { "#000000", "#000000",
			"#000000", "#FFFFFF", "#FFFFFF" };
	public static String[] greys6Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#FFFFFF", "#FFFFFF" };
	public static String[] greys7Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#FFFFFF", "#FFFFFF", "#FFFFFF" };
	public static String[] greys8Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#FFFFFF", "#FFFFFF", "#FFFFFF" };
	public static String[] greys9Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#FFFFFF", "#FFFFFF", "#FFFFFF",
			"#FFFFFF" };
	public static String[] oranges3Font = new String[] { "#000000", "#000000",
			"#000000" };
	public static String[] oranges4Font = new String[] { "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] oranges5Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] oranges6Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] oranges7Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] oranges8Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] oranges9Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#FFFFFF" };
	public static String[] orrd3Font = new String[] { "#000000", "#000000", "#000000" };
	public static String[] orrd4Font = new String[] { "#000000", "#000000", "#000000",
			"#000000" };
	public static String[] orrd5Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] orrd6Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] orrd7Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] orrd8Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] orrd9Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] paired3Font = new String[] { "#000000", "#000000",
			"#000000" };
	public static String[] paired4Font = new String[] { "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] paired5Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] paired6Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] paired7Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] paired8Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] paired9Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000" };
	public static String[] paired10Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] paired11Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] paired12Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] pastel13Font = new String[] { "#000000", "#000000",
			"#000000" };
	public static String[] pastel14Font = new String[] { "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] pastel15Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] pastel16Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] pastel17Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] pastel18Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] pastel19Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000" };
	public static String[] pastel23Font = new String[] { "#000000", "#000000",
			"#000000" };
	public static String[] pastel24Font = new String[] { "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] pastel25Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] pastel26Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] pastel27Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] pastel28Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] piyg3Font = new String[] { "#000000", "#000000", "#000000" };
	public static String[] piyg4Font = new String[] { "#000000", "#000000", "#000000",
			"#000000" };
	public static String[] piyg5Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] piyg6Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] piyg7Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] piyg8Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] piyg9Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] piyg10Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000", "#FFFFFF" };
	public static String[] piyg11Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000", "#000000", "#FFFFFF" };
	public static String[] prgn3Font = new String[] { "#000000", "#000000", "#000000" };
	public static String[] prgn4Font = new String[] { "#000000", "#000000", "#000000",
			"#000000" };
	public static String[] prgn5Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] prgn6Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#FFFFFF" };
	public static String[] prgn7Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] prgn8Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] prgn9Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] prgn10Font = new String[] { "#FFFFFF", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#FFFFFF", "#FFFFFF" };
	public static String[] prgn11Font = new String[] { "#FFFFFF", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000", "#FFFFFF", "#FFFFFF" };
	public static String[] pubu3Font = new String[] { "#000000", "#000000", "#000000" };
	public static String[] pubu4Font = new String[] { "#000000", "#000000", "#000000",
			"#000000" };
	public static String[] pubu5Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] pubu6Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] pubu7Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] pubu8Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] pubu9Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] pubugn3Font = new String[] { "#000000", "#000000",
			"#000000" };
	public static String[] pubugn4Font = new String[] { "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] pubugn5Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#FFFFFF" };
	public static String[] pubugn6Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] pubugn7Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] pubugn8Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] pubugn9Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#FFFFFF",
			"#FFFFFF" };
	public static String[] puor3Font = new String[] { "#000000", "#000000", "#000000" };
	public static String[] puor4Font = new String[] { "#000000", "#000000", "#000000",
			"#000000" };
	public static String[] puor5Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] puor6Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] puor7Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] puor8Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] puor9Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] purd3Font = new String[] { "#000000", "#000000", "#000000" };
	public static String[] purd4Font = new String[] { "#000000", "#000000", "#000000",
			"#000000" };
	public static String[] purd5Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] purd6Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] purd7Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] purd8Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] purd9Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] puor10Font = new String[] { "#FFFFFF", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000", "#FFFFFF" };
	public static String[] puor11Font = new String[] { "#FFFFFF", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000", "#000000", "#FFFFFF" };
	public static String[] purples3Font = new String[] { "#000000", "#000000",
			"#000000" };
	public static String[] purples4Font = new String[] { "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] purples5Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] purples6Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] purples7Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] purples8Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] purples9Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#FFFFFF" };
	public static String[] rdbu10Font = new String[] { "#FFFFFF", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000", "#FFFFFF" };
	public static String[] rdbu11Font = new String[] { "#FFFFFF", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000", "#000000", "#FFFFFF" };
	public static String[] rdbu3Font = new String[] { "#000000", "#000000", "#000000" };
	public static String[] rdbu4Font = new String[] { "#000000", "#000000", "#000000",
			"#000000" };
	public static String[] rdbu5Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] rdbu6Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] rdbu7Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] rdbu8Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] rdbu9Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] rdgy3Font = new String[] { "#000000", "#000000", "#000000" };
	public static String[] rdgy4Font = new String[] { "#000000", "#000000", "#000000",
			"#FFFFFF" };
	public static String[] rdgy5Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#FFFFFF" };
	public static String[] rdgy6Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#FFFFFF" };
	public static String[] rdgy7Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] rdgy8Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] rdgy9Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] rdpu3Font = new String[] { "#000000", "#000000", "#000000" };
	public static String[] rdpu4Font = new String[] { "#000000", "#000000", "#000000",
			"#000000" };
	public static String[] rdpu5Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#FFFFFF" };
	public static String[] rdpu6Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#FFFFFF" };
	public static String[] rdpu7Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] rdpu8Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] rdpu9Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#FFFFFF", "#FFFFFF" };
	public static String[] rdgy10Font = new String[] { "#FFFFFF", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#FFFFFF", "#FFFFFF" };
	public static String[] rdgy11Font = new String[] { "#FFFFFF", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000", "#FFFFFF", "#FFFFFF" };
	public static String[] rdylbu3Font = new String[] { "#000000", "#000000",
			"#000000" };
	public static String[] rdylbu4Font = new String[] { "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] rdylbu5Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] rdylbu6Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] rdylbu7Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] rdylbu8Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] rdylbu9Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000" };
	public static String[] rdylbu10Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] rdylbu11Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] rdylgn3Font = new String[] { "#000000", "#000000",
			"#000000" };
	public static String[] rdylgn4Font = new String[] { "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] rdylgn5Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] rdylgn6Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] rdylgn7Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] rdylgn8Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] rdylgn9Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000" };
	public static String[] rdylgn10Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000", "#FFFFFF" };
	public static String[] rdylgn11Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000", "#000000", "#FFFFFF" };
	public static String[] reds3Font = new String[] { "#000000", "#000000", "#000000" };
	public static String[] reds4Font = new String[] { "#000000", "#000000", "#000000",
			"#000000" };
	public static String[] reds5Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] reds6Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] reds7Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] reds8Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] reds9Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] set13Font = new String[] { "#000000", "#000000", "#000000" };
	public static String[] set14Font = new String[] { "#000000", "#000000", "#000000",
			"#000000" };
	public static String[] set15Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] set16Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] set17Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] set18Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] set19Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] set23Font = new String[] { "#000000", "#000000", "#000000" };
	public static String[] set24Font = new String[] { "#000000", "#000000", "#000000",
			"#000000" };
	public static String[] set25Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] set26Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] set27Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] set28Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] set33Font = new String[] { "#000000", "#000000", "#000000" };
	public static String[] set34Font = new String[] { "#000000", "#000000", "#000000",
			"#000000" };
	public static String[] set35Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] set36Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] set37Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] set38Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] set39Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] set310Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] set311Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] set312Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] spectral3Font = new String[] { "#000000", "#000000",
			"#000000" };
	public static String[] spectral4Font = new String[] { "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] spectral5Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] spectral6Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] spectral7Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] spectral8Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] spectral9Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000" };
	public static String[] spectral10Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] spectral11Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] ylgn3Font = new String[] { "#000000", "#000000", "#000000" };
	public static String[] ylgn4Font = new String[] { "#000000", "#000000", "#000000",
			"#000000" };
	public static String[] ylgn5Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#FFFFFF" };
	public static String[] ylgn6Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#FFFFFF" };
	public static String[] ylgn7Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] ylgn8Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#FFFFFF" };
	public static String[] ylgn9Font = new String[] { "#000000", "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#FFFFFF", "#FFFFFF" };
	public static String[] ylgnbu3Font = new String[] { "#000000", "#000000",
			"#000000" };
	public static String[] ylgnbu4Font = new String[] { "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] ylgnbu5Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] ylgnbu6Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] ylgnbu7Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] ylgnbu8Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] ylgnbu9Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#FFFFFF" };
	public static String[] ylorbr3Font = new String[] { "#000000", "#000000",
			"#000000" };
	public static String[] ylorbr4Font = new String[] { "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] ylorbr5Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] ylorbr6Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] ylorbr7Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] ylorbr8Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] ylorbr9Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#FFFFFF" };
	public static String[] ylorrd3Font = new String[] { "#000000", "#000000",
			"#000000" };
	public static String[] ylorrd4Font = new String[] { "#000000", "#000000",
			"#000000", "#000000" };
	public static String[] ylorrd5Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000" };
	public static String[] ylorrd6Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000" };
	public static String[] ylorrd7Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] ylorrd8Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000" };
	public static String[] ylorrd9Font = new String[] { "#000000", "#000000",
			"#000000", "#000000", "#000000", "#000000", "#000000", "#000000",
			"#FFFFFF" };

}
