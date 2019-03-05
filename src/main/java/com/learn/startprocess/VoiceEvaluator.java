//公司实例文件 -- 由于有些包未引入，注释代码

//package cn.com.hhkid.talk.util;
//
//public class VoiceEvaluator {
//	public static double[] iFlySpeechEvaluation(String iseType, String text, String lang, String voicePath) {
//		// 默认按英文句子处理
//		String sType = "en_sentence";
//		String textPre = "";
//		String textFinal = new String(text);
//		String scorePath = "read_sentence/rec_paper/read_chapter/sentence";
//		if (Constant.LANG_EN.equals(lang)) {
//			if (textFinal.indexOf(" ") < 0) {
//				// 英文、单词
//				sType = "en_word";
//				textPre = "[word]";
//				textFinal = textFinal.replaceAll("\\W", "");
//				scorePath = "read_word/rec_paper/read_word.total_score";
//			} else {
//				// 英文、句子，取sentence元素下所有word元素的total_score
//			}
//		} else if (Constant.LANG_ZH_CN.equals(lang)) {
//			if (textFinal.indexOf("，") < 0 && textFinal.indexOf("。") < 0 && textFinal.indexOf("！") < 0) {
//				// 中文、词
//				sType = "cn_word";
//				scorePath = "read_word/rec_paper/read_word.total_score";
//			} else {
//				// 中文、句子
//				sType = "cn_sentence";
//				scorePath = "read_sentence/rec_paper/read_sentence.total_score";
//			}
//		}
//		String cmdFolder = Constant.WEB_PATH + "ise" + java.io.File.separator + "linux";
//		String cmdName = "hhkid.sh";
//		if (Constant.GLOBALSETTING_ISE_TYPE_WIN.equalsIgnoreCase(iseType)) {
//			cmdFolder = Constant.WEB_PATH + "ise" + java.io.File.separator + "win";
//			cmdName = "hhkid.exe";
//		} else if (Constant.GLOBALSETTING_ISE_TYPE_DEBUG.equalsIgnoreCase(iseType)) {
//			java.io.File file = new java.io.File(voicePath);
//			if (file.length() > 50 * 1024) {
//				return new double[]{new Double(3.0).doubleValue()};
//			} else {
//				return new double[]{new Double(1.0).doubleValue()};
//			}
//		}
//		java.io.File folder = new java.io.File(cmdFolder);
//		if (!folder.exists() || !folder.isDirectory()) {
//			System.out.println("folder: " + folder.exists() + ", " + folder.isDirectory());
//		}
//		java.io.File file = new java.io.File(voicePath);
//		if (!file.exists()) {
//			System.out.println("file: " + file.exists());
//		}
//		String cmd[] = {cmdFolder + java.io.File.separator + cmdName, sType, textPre + textFinal, voicePath};
//		System.out.println("cmd: " + cmd[0] + " " + cmd[1] + " " + cmd[2] + " " + cmd[3]);
//		if (Constant.GLOBALSETTING_ISE_TYPE_LINUX.equalsIgnoreCase(iseType)) {
//			try {
//				// linux下，utf8前增加bom头（讯飞接口中，输入的文本需要带bom头的utf8）
//				// windows下本来就有，所以不需要增加
//				java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
//				baos.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
//				byte[] bytes = cmd[2].getBytes();
//				for (int i = 0; i < bytes.length; i ++) {
//					baos.write(bytes[i]);
//				}
//				cmd[2] = baos.toString("utf-8");
//				baos.close();
//			} catch (java.io.UnsupportedEncodingException e) {
//				e.printStackTrace();
//			} catch (java.io.IOException e) {
//				e.printStackTrace();
//			}
//		}
//		StringBuffer outIS = new StringBuffer();
//		try {
//			final Process process = java.lang.Runtime.getRuntime().exec(cmd, null, folder);
//			// 读取输出内容
//			java.io.BufferedReader brIS = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()));
//			java.io.BufferedReader brES = new java.io.BufferedReader(new java.io.InputStreamReader(process.getErrorStream()));
//			String lineIS = null, lineES = null;
//			StringBuffer outES = new StringBuffer();
//			while ((lineIS = brIS.readLine()) != null) {
//				outIS.append(lineIS);
//			}
//			while ((lineES = brES.readLine()) != null) {
//				outES.append(lineES);
//			}
//			brIS.close();
//			process.getInputStream().close();
//			brES.close();
//			process.getErrorStream().close();
//			try {
//				process.waitFor();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			System.out.println("iFlySpeechEvaluation: IS");
//			System.out.println(outIS.toString());
//			System.out.println("iFlySpeechEvaluation: ES");
//			System.out.println(outES.toString());
//		} catch (java.io.IOException e) {
//			e.printStackTrace();
//			throw new Exception(Constant.ERR_ISE_IOEXCEPTION, "iFlySpeechEvaluation Error 1: IOException");
//		}
//		if (outIS.length() < 1) {
//			throw new Exception(Constant.ERR_ISE_NORESULT, "iFlySpeechEvaluation Error 2: outIS.length() < 1");
//		} else if (outIS.length() > 13) {
//			int errorCode = 0;
//			if ("QISEGetResult".equalsIgnoreCase(outIS.substring(0, 13))) {
//				// GetResult时出现错误
//				int idx = outIS.lastIndexOf("error = ", 13);
//				if (idx > -1) {
//					idx += 8;
//					try {
//						String errorCodeStr = outIS.substring(idx);
//						errorCode = new Integer(errorCodeStr);
//					} catch (java.lang.Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//			if (errorCode > 0) {
//				if (errorCode == 11401) {
//					throw new Exception(Constant.ERR_ISE_GETERR_SILENCE, outIS.toString());
//				} else {
//					throw new Exception(Constant.ERR_ISE_GETERR_OTHER, errorCode + "");
//				}
//			}
//		}
//		// 读取分值
//		// TODO: 多个词的时候，某个词如果为0分，整句也应“不合格”
//		org.dom4j.Document doc = Xml.getXmlDocByString(outIS.toString());
//		if (doc == null) {
//			throw new Exception(Constant.ERR_ISE_NOXMLDOC, "iFlySpeechEvaluation Error 3: XMLUtil.getXmlDocByString(outIS.toString()) return null. " + outIS.toString());
//		}
//		if (sType.equalsIgnoreCase("en_sentence")) {
//			// 英文、句子，取sentence元素下所有word元素的total_score
//			org.dom4j.Element sentenceEle = Xml.getElementByPath(doc, scorePath);
//			if (sentenceEle == null) {
//				throw new Exception(Constant.ERR_ISE_NOXMLVALUE, "iFlySpeechEvaluation Error 4: XMLUtil.getElementByPath(doc, scorePath) return null, scorePath=" + scorePath);
//			}
//			@SuppressWarnings("unchecked")
//			java.util.Iterator<org.dom4j.Element> wordEles = sentenceEle.elementIterator("word");
//			int iWordCount = new Integer(sentenceEle.attributeValue("word_count")).intValue(),
//				i = 0;
//			double[] dValues = new double[iWordCount];
//			while (wordEles.hasNext()) {
//				org.dom4j.Element wordEle = wordEles.next();
//				if (wordEle.attributeValue("total_score") != null) { // sentence中会出现word.content为"sil"的节点，此类节点不属于word，也没有total_score属性
//					dValues[i] = new Double(wordEle.attributeValue("total_score")).doubleValue();
//					i ++;
//				}
//			}
//			return dValues;
//		} else {
//			String sValue = Xml.getValueByPath(doc, scorePath);
//			if (sValue == null || "".equalsIgnoreCase(sValue)) {
//				throw new Exception(Constant.ERR_ISE_NOXMLVALUE, "iFlySpeechEvaluation Error 4: XMLUtil.getValueByPath(doc, scorePath) return null, scorePath=" + scorePath);
//			}
//			return new double[]{new Double(sValue).doubleValue()};
//		}
//	}
//
//	public static double[] iFlySpeechEvaluation(String iseType, java.util.ArrayList<String> texts, String lang, String voicePath) {
//		if (texts == null || texts.size() < 1) {
//			return new double[]{0.0};
//		}
//		if (texts.size() == 1) {
//			return iFlySpeechEvaluation(iseType, texts.get(0), lang, voicePath);
//		} else {
//			// 输入多组词句的情况
//			// TODO: 如果词句中包括多个词，则暂时只支持返回一个分值
//			double dValue = 0.0;
//			for (int i = 0; i < texts.size(); i ++) {
//				double[] v = iFlySpeechEvaluation(iseType, texts.get(i), lang, voicePath);
//				double vSmall = 5.0;
//				if (v.length < 1) {
//					vSmall = 0.0;
//				}
//				for (int j = 0; j < v.length; j ++) {
//					// 若一组词句中有多个词的分数，取最小的
//					if (v[j] < vSmall) {
//						vSmall = v[j];
//					}
//				}
//				// 在各组词句中取分数最高的
//				if (vSmall > dValue) {
//					dValue = vSmall;
//				}
//			}
//			return new double[]{dValue};
//		}
//	}
//}
