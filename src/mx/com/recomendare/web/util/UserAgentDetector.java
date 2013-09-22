/**
 * 
 */
package mx.com.recomendare.web.util;

/**
 * @author jerry
 *
 */
public final class UserAgentDetector {

	private static String getVersionNumber(String a_userAgent, int a_position) {
		if (a_position<0) return "";
		StringBuffer res = new StringBuffer();
		int status = 0;

		while (a_position < a_userAgent.length()) {
			char c = a_userAgent.charAt(a_position);
			switch (status) {
			case 0: // No valid digits encountered yet
				if (c == ' ' || c=='/') break;
				if (c == ';' || c==')') return "";
				status = 1;
			case 1: // Version number in progress
				if (c == ';' || c=='/' || c==')' || c=='(' || c=='[') return res.toString().trim();
				if (c == ' ') status = 2;
				res.append(c);
				break;
			case 2: // Space encountered - Might need to end the parsing
				if ((Character.isLetter(c) && 
						Character.isLowerCase(c)) ||
						Character.isDigit(c)) {
					res.append(c);
					status=1;
				} else
					return res.toString().trim();
				break;
			}
			a_position++;
		}
		return res.toString().trim();
	}

	private static String getFirstVersionNumber(String a_userAgent, int a_position, int numDigits) {
		String ver = getVersionNumber(a_userAgent, a_position);
		if (ver==null) return "";
		int i = 0;
		String res="";
		while (i<ver.length() && i<numDigits) {
			res+=String.valueOf(ver.charAt(i));
			i++;
		}
		return res;
	}

	private static String[]getArray(String a, String b, String c) {
		String[]res = new String[3];
		res[0]=a;
		res[1]=b;
		res[2]=c;
		return res;
	}

	public static String[] getBotName(String userAgent) {
		userAgent = userAgent.toLowerCase();
		int pos=0;
		String res=null;
		if ((pos=userAgent.indexOf("google/"))>-1) {
			res= "Google";
			pos+=7;
		} else
			if ((pos=userAgent.indexOf("msnbot/"))>-1) {
				res= "MSNBot";
				pos+=7;
			} else
				if ((pos=userAgent.indexOf("googlebot/"))>-1) {
					res= "Google";
					pos+=10;
				} else
					if ((pos=userAgent.indexOf("webcrawler/"))>-1) {
						res= "WebCrawler";
						pos+=11;
					} else
						// The following two bots don't have any version number in their User-Agent strings.
						if ((pos=userAgent.indexOf("inktomi"))>-1) {
							res= "Inktomi";
							pos=-1;
						} else
							if ((pos=userAgent.indexOf("teoma"))>-1) {
								res= "Teoma";
								pos=-1;
							}
		if (res==null) return null;
		return getArray(res,res,res + getVersionNumber(userAgent,pos));
	}

	public static String[] getOS(String userAgent) {
		if (getBotName(userAgent)!=null) return getArray("Bot","Bot","Bot");
		String[]res = null;
		int pos;
		if ((pos=userAgent.indexOf("Windows-NT"))>-1) {
			res = getArray("Win","WinNT","Win"+getVersionNumber(userAgent,pos+8));
		} else
			if (userAgent.indexOf("Windows NT")>-1) {
				// The different versions of Windows NT are decoded in the verbosity level 2
				// ie: Windows NT 5.1 = Windows XP
				if ((pos=userAgent.indexOf("Windows NT 5.1"))>-1) {
					res = getArray("Win","WinXP","Win"+getVersionNumber(userAgent,pos+7));
				} else
					if ((pos=userAgent.indexOf("Windows NT 5.0"))>-1) {
						res = getArray("Win","Win2000","Win"+getVersionNumber(userAgent,pos+7));
					} else
						if ((pos=userAgent.indexOf("Windows NT 5.2"))>-1) {
							res = getArray("Win","Win2003","Win"+getVersionNumber(userAgent,pos+7));
						} else
							if ((pos=userAgent.indexOf("Windows NT 4.0"))>-1) {
								res = getArray("Win","WinNT4","Win"+getVersionNumber(userAgent,pos+7));
							} else
								if ((pos=userAgent.indexOf("Windows NT)"))>-1) {
									res = getArray("Win","WinNT","WinNT");
								} else
									if ((pos=userAgent.indexOf("Windows NT;"))>-1) {
										res = getArray("Win","WinNT","WinNT");
									} else
										res = getArray("Win","<B>WinNT?</B>","<B>WinNT?</B>");
			} else
				if (userAgent.indexOf("Win")>-1) {
					if (userAgent.indexOf("Windows")>-1) {
						if ((pos=userAgent.indexOf("Windows 98"))>-1) {
							res = getArray("Win","Win98","Win"+getVersionNumber(userAgent,pos+7));
						} else
							if ((pos=userAgent.indexOf("Windows_98"))>-1) {
								res = getArray("Win","Win98","Win"+getVersionNumber(userAgent,pos+8));
							} else
								if ((pos=userAgent.indexOf("Windows 2000"))>-1) {
									res = getArray("Win","Win2000","Win"+getVersionNumber(userAgent,pos+7));
								} else
									if ((pos=userAgent.indexOf("Windows 95"))>-1) {
										res = getArray("Win","Win95","Win"+getVersionNumber(userAgent,pos+7));
									} else
										if ((pos=userAgent.indexOf("Windows 9x"))>-1) {
											res = getArray("Win","Win9x","Win"+getVersionNumber(userAgent,pos+7));
										} else
											if ((pos=userAgent.indexOf("Windows ME"))>-1) {
												res = getArray("Win","WinME","Win"+getVersionNumber(userAgent,pos+7));
											} else
												if ((pos=userAgent.indexOf("Windows 3.1"))>-1) {
													res = getArray("Win","Win31","Win"+getVersionNumber(userAgent,pos+7));
												}
						// If no version was found, rely on the following code to detect "WinXX"
						// As some User-Agents include two references to Windows
						// Ex: Mozilla/5.0 (Windows; U; Win98; en-US; rv:1.5)
					}
					if (res == null) {
						if ((pos=userAgent.indexOf("Win98"))>-1) {
							res = getArray("Win","Win98","Win"+getVersionNumber(userAgent,pos+3));
						} else
							if ((pos=userAgent.indexOf("Win31"))>-1) {
								res = getArray("Win","Win31","Win"+getVersionNumber(userAgent,pos+3));
							} else
								if ((pos=userAgent.indexOf("Win95"))>-1) {
									res = getArray("Win","Win95","Win"+getVersionNumber(userAgent,pos+3));
								} else
									if ((pos=userAgent.indexOf("Win 9x"))>-1) {
										res = getArray("Win","Win9x","Win"+getVersionNumber(userAgent,pos+3));
									} else
										if ((pos=userAgent.indexOf("WinNT4.0"))>-1) {
											res = getArray("Win","WinNT4","Win"+getVersionNumber(userAgent,pos+3));
										} else
											if ((pos=userAgent.indexOf("WinNT"))>-1) {
												res = getArray("Win","WinNT","Win"+getVersionNumber(userAgent,pos+3));
											}
					}
					if (res == null) {
						if ((pos=userAgent.indexOf("Windows"))>-1) {
							res = getArray("Win","<B>Win?</B>","<B>Win?"+getVersionNumber(userAgent,pos+7)+"</B>");
						} else
							if ((pos=userAgent.indexOf("Win"))>-1) {
								res = getArray("Win","<B>Win?</B>","<B>Win?"+getVersionNumber(userAgent,pos+3)+"</B>");
							} else
								// Should not happen at this point
								res = getArray("Win","<B>Win?</B>","<B>Win?</B>");
					}
				} else
					if ((pos=userAgent.indexOf("Mac OS X"))>-1) {
						res = getArray("Mac","MacOSX","MacOS"+getVersionNumber(userAgent,pos+6));
					} else
						if ((pos=userAgent.indexOf("Mac_PowerPC"))>-1) {
							res = getArray("Mac","MacPPC","MacOS"+getVersionNumber(userAgent,pos+3));
						} else
							if ((pos=userAgent.indexOf("FreeBSD"))>-1) {
								res = getArray("FreeBSD","FreeBSD","FreeBSD"+getVersionNumber(userAgent,pos+7));
							} else
								if ((pos=userAgent.indexOf("Linux"))>-1) {
									res = getArray("Linux","Linux","Linux"+getVersionNumber(userAgent,pos+5));
								} else
									if ((pos=userAgent.indexOf("Unix"))>-1) {
										res = getArray("Linux","Linux","Linux"+getVersionNumber(userAgent,pos+4));
									} else
										if ((pos=userAgent.indexOf("SunOS"))>-1) {
											res = getArray("SunOS","SunOS","SunOS"+getVersionNumber(userAgent,pos+5));
										} else
											res = getArray("<b>?</b>","<b>?</b>","<b>?</b>");
		return res;
	}

	public static String []getBrowser(String userAgent) {
		String []botName;
		if ((botName=getBotName(userAgent))!=null) return botName;
		String[]res = null;
		int pos;
		if ((pos=userAgent.indexOf("Lotus-Notes/"))>-1) {
			res = getArray("LotusNotes","LotusNotes","LotusNotes"+getVersionNumber(userAgent,pos+12));
		} else
			if ((pos=userAgent.indexOf("Opera"))>-1) {
				res = getArray("Opera","Opera"+getFirstVersionNumber(userAgent,pos+5,1),"Opera"+getVersionNumber(userAgent,pos+5));
			} else
				if (userAgent.indexOf("MSIE")>-1) {
					if ((pos=userAgent.indexOf("MSIE 6.0"))>-1) {
						res = getArray("MSIE","MSIE6","MSIE"+getVersionNumber(userAgent,pos+4));
					} else
						if ((pos=userAgent.indexOf("MSIE 5.0"))>-1) {
							res = getArray("MSIE","MSIE5","MSIE"+getVersionNumber(userAgent,pos+4));
						} else
							if ((pos=userAgent.indexOf("MSIE 5.5"))>-1) {
								res = getArray("MSIE","MSIE5.5","MSIE"+getVersionNumber(userAgent,pos+4));
							} else
								if ((pos=userAgent.indexOf("MSIE 5."))>-1) {
									res = getArray("MSIE","MSIE5.x","MSIE"+getVersionNumber(userAgent,pos+4));
								} else
									if ((pos=userAgent.indexOf("MSIE 4"))>-1) {
										res = getArray("MSIE","MSIE4","MSIE"+getVersionNumber(userAgent,pos+4));
									} else
										res = getArray("MSIE","<B>MSIE?</B>","<B>MSIE?"+getVersionNumber(userAgent,userAgent.indexOf("MSIE")+4)+"</B>");
				} else
					if ((pos=userAgent.indexOf("Gecko/"))>-1) {
						res = getArray("Gecko","Gecko","Gecko"+getFirstVersionNumber(userAgent,pos+5,4));
						if ((pos=userAgent.indexOf("Camino/"))>-1) {
							res[1]+="(Camino)";
							res[2]+="(Camino"+getVersionNumber(userAgent,pos+7)+")";
						} else
							if ((pos=userAgent.indexOf("Chimera/"))>-1) {
								res[1]+="(Chimera)";
								res[2]+="(Chimera"+getVersionNumber(userAgent,pos+8)+")";
							} else
								if ((pos=userAgent.indexOf("Firebird/"))>-1) {
									res[1]+="(Firebird)";
									res[2]+="(Firebird"+getVersionNumber(userAgent,pos+9)+")";
								} else
									if ((pos=userAgent.indexOf("Phoenix/"))>-1) {
										res[1]+="(Phoenix)";
										res[2]+="(Phoenix"+getVersionNumber(userAgent,pos+8)+")";
									} else
										if ((pos=userAgent.indexOf("Galeon/"))>-1) {
											res[1]+="(Galeon)";
											res[2]+="(Galeon"+getVersionNumber(userAgent,pos+7)+")";
										} else
											if ((pos=userAgent.indexOf("Firefox/"))>-1) {
												res[1]+="(Firefox)";
												res[2]+="(Firefox"+getVersionNumber(userAgent,pos+8)+")";
											} else
												if ((pos=userAgent.indexOf("Netscape/"))>-1) {
													if ((pos=userAgent.indexOf("Netscape/6"))>-1) {
														res[1]+="(NS6)";
														res[2]+="(NS"+getVersionNumber(userAgent,pos+9)+")";
													} else
														if ((pos=userAgent.indexOf("Netscape/7"))>-1) {
															res[1]+="(NS7)";
															res[2]+="(NS"+getVersionNumber(userAgent,pos+9)+")";
														} else {
															res[1]+="(NS?)";
															res[2]+="(NS?"+getVersionNumber(userAgent,userAgent.indexOf("Netscape/")+9)+")";
														}
												}
					} else
						if ((pos=userAgent.indexOf("Netscape/"))>-1) {
							if ((pos=userAgent.indexOf("Netscape/4"))>-1) {
								res = getArray("NS","NS4","NS"+getVersionNumber(userAgent,pos+9));
							} else
								res = getArray("NS","NS?","NS?"+getVersionNumber(userAgent,pos+9));
						} else
							if ((pos=userAgent.indexOf("Safari/"))>-1) {
								res = getArray("KHTML","KHTML(Safari)","KHTML(Safari"+getVersionNumber(userAgent,pos+6)+")");
							} else
								if ((pos=userAgent.indexOf("Konqueror/"))>-1) {
									res = getArray("KHTML","KHTML(Konqueror)","KHTML(Konqueror"+getVersionNumber(userAgent,pos+9)+")");
								} else
									if ((pos=userAgent.indexOf("KHTML"))>-1) {
										res = getArray("KHTML","KHTML?","KHTML?("+getVersionNumber(userAgent,pos+5)+")");
									} else
										// We will interpret Mozilla/4.x as Netscape Communicator is and only if x
										// is not 0 or 5
										if (userAgent.indexOf("Mozilla/4.")==0 &&
												userAgent.indexOf("Mozilla/4.0")<0 &&
												userAgent.indexOf("Mozilla/4.5 ")<0) {
											res = getArray("Communicator","Communicator","Communicator"+getVersionNumber(userAgent,pos+8));
										} else
											return getArray("<B>?</B>","<B>?</B>","<B>?</B>");
		return res;
	}
	
}//END OF FILE