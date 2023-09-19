package com.example.demo.common.sqlcommon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.Tuple;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Phuong thuc chung cho toan bo project
 */
public class FnCommon {
    private static final Logger LOGGER = LoggerFactory.getLogger(FnCommon.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    /**
     * Convert date date to string date
     *
     * @param date
     * @param isFullDateTime:true: full date time, false: date sort
     * @return
     */
    public static String convertDateToString(Date date, Boolean isFullDateTime) {
        String strDate;
        if (date == null) {
            return "";
        }
        if (isFullDateTime) {
            strDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);
        } else {
            strDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
        }
        return strDate;
    }

    /**
     * Convert date to string without separator
     *
     * @param date
     * @param isFullDateTime:true: full date time, false: date sort
     * @return
     */
    public static String convertDateToStringWithoutSeparator(Date date, Boolean isFullDateTime) {
        String strDate;
        if (date == null) {
            return "";
        }
        if (isFullDateTime) {
            strDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
        } else {
            strDate = new SimpleDateFormat("yyyyMMdd").format(date);
        }
        return strDate;
    }

    /**
     * Convert string date to date date
     *
     * @param strDate
     * @param isFullDateTime:true: full date time, false: date sort
     * @return
     */
    public static Date convertStringToDate(String strDate, Boolean isFullDateTime) {
        if (strDate == null || "".equals(strDate)) {
            return null;
        }
        if (isFullDateTime) {
            if (strDate.length() != "dd/MM/yyyy HH:mm:ss".length()) {
                return null;
            }
        } else {
            if (strDate.length() != "dd/MM/yyyy".length()) {
                return null;
            }
        }
        try {
            Date date;
            if (isFullDateTime) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                simpleDateFormat.setLenient(false);
                date = simpleDateFormat.parse(strDate);
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                simpleDateFormat.setLenient(false);
                date = simpleDateFormat.parse(strDate);
            }
            return date;
        } catch (ParseException e) {
            LOGGER.error("Loi! convertStringToDate: " + e.getMessage());
        }
        return null;
    }

    /**
     * Convert string date to sql date date
     *
     * @param strDate
     * @param isFullDateTime:true: full date time, false: date sort
     * @return
     */
    public static java.sql.Date convertStringToSqlDate(String strDate, Boolean isFullDateTime) {
        if (strDate == null || "".equals(strDate)) {
            return null;
        }
        try {
            Date date;
            if (isFullDateTime) {
                date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(strDate);
            } else {
                date = new SimpleDateFormat("dd/MM/yyyy").parse(strDate);
            }
            return new java.sql.Date(date.getTime());
        } catch (ParseException e) {
            LOGGER.error("Loi! convertStringToDate: " + e.getMessage());
        }
        return null;
    }

    public static String removeSpace(String s) {
        return StringUtils.deleteWhitespace(s);
    }

    public static String makeSlug(String s) {
        String nowhitespace = Pattern.compile("[\\s]").matcher(s).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = Pattern.compile("[^\\w-]").matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }

    public static String getNumberAndDotFromString(String strInput) {
        String digits = strInput.replaceAll("[^0-9./:]", "");
        return digits;
    }
    public static Object convertJsonToObject(String strJsonData, Class<?> classOfT) {
        Object result = null;
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            JsonReader reader = new JsonReader(new StringReader(strJsonData));
            reader.setLenient(true);
            result = gson.fromJson(reader, classOfT);
        } catch (JsonSyntaxException | JsonIOException var6) {
            if ("401".equals(strJsonData) && "404".equals(strJsonData)) {
                LOGGER.error("Mat ket noi he thong nhan su! ket noi lai");
            } else {
                LOGGER.error("Loi convert json:" + strJsonData);
                LOGGER.error("Loi! jsonGetItem: " + var6.getMessage());
            }
        }
        return result;
    }

    public static List<?> convertToEntity(List<Tuple> input, Class<?> dtoClass) {
        List<Object> arrayList = new ArrayList();
        input.stream().forEach((tuple) -> {
            Map<String, Object> temp = new HashMap();
            tuple.getElements().stream().forEach((tupleElement) -> {
                temp.put(tupleElement.getAlias().toLowerCase(), tuple.get(tupleElement.getAlias()));
            });
            ObjectMapper map = new ObjectMapper();
            map.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            map.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            try {
                String mapToString = map.writeValueAsString(temp);
                arrayList.add(map.readValue(mapToString, dtoClass));
            } catch (JsonProcessingException var6) {
                throw new RuntimeException(var6.getMessage());
            }
        });
        return arrayList;
    }

    private static HttpServletRequest getCurrentRequest() {
        RequestAttributes attribs = RequestContextHolder.getRequestAttributes();
        if (attribs != null) {
            return ((ServletRequestAttributes) attribs).getRequest();
        }
        return null;
    }


    /**
     * Convert byte[] to string
     *
     * @param bytes
     * @return
     */
    public static String convertByteArrayToString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Convert string to byte[]
     *
     * @param str
     * @return
     */
    public static byte[] convertStringToByteArray(String str) {
        if (str == null || "".equals(str.trim())) {
            return null;
        }
        return Base64.getDecoder().decode(str);
    }

    public static String convertObjectToStringJson(Object object) {
        String strMess = "";
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            strMess = gson.toJson(object);
        } catch (Exception e) {
            LOGGER.error("Loi! FuctionCommon.convertObjectToStringJson", e);
        }
        return strMess;
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * Check string is blank
     *
     * @param str
     * @return
     */
    public static boolean isStringBlank(String str) {
        return str == null || "".equals(str.trim());
    }

    /**
     * Check string is not blank
     *
     * @param str
     * @return
     */
    public static boolean isStringNotBlank(String str) {
        return str != null && !"".equals(str.trim());
    }

    /**
     * Check is date
     *
     * @param strDate
     * @param isFullDateTime:true: full date time, false: date sort
     * @return
     */
    public static boolean isDate(String strDate, Boolean isFullDateTime) {
        if (strDate == null || "".equals(strDate)) {
            return false;
        }
        try {
            if (isFullDateTime) {
                if (strDate.length() == "dd/MM/yyyy HH:mm:ss".length()) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    simpleDateFormat.setLenient(false);
                    simpleDateFormat.parse(strDate);
                } else {
                    return false;
                }
            } else {
                if (strDate.length() == "dd/MM/yyyy".length()) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    simpleDateFormat.setLenient(false);
                    simpleDateFormat.parse(strDate);
                } else {
                    return false;
                }
            }
            return true;
        } catch (ParseException e) {
            LOGGER.error("Loi! convertStringToDate: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check is not date
     *
     * @param strDate
     * @param isFullDateTime:true: full date time, false: date sort
     * @return
     */
    public static boolean isNotDate(String strDate, Boolean isFullDateTime) {
        return !isDate(strDate, isFullDateTime);
    }

    /**
     * Convert util date to sql date
     *
     * @param date
     * @return
     */
    public static java.sql.Date convertUtilDateToSqlDate(Date date) {
        if (date == null) {
            return null;
        }
        return new java.sql.Date(date.getTime());
    }



    public static byte[] convertFileToByte(InputStream fis) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                outputStream.write(buf, 0, readNum);
            }
        } finally {
            fis.close();
        }
        byte[] bytes = outputStream.toByteArray();
        outputStream.flush();
        outputStream.close();
        return bytes;
    }

    /**
     * Save temp file
     *
     * @param originalFilename
     * @param bytes
     * @param tempFolder
     * @param date
     * @return
     */
    public static String saveTempFile(String originalFilename, byte[] bytes, String tempFolder,
                                      Date date)
            throws IOException {
        if (date == null) {
            date = new Date();
        }
        originalFilename = originalFilename.replaceAll("[\\\\/:*?\"<>|%]", "");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String fileName = calendar.get(Calendar.HOUR_OF_DAY) + ""
                + calendar.get(Calendar.MINUTE) + "" + calendar.get(Calendar.SECOND)
                + "" + calendar.get(Calendar.MILLISECOND) + "_" + originalFilename;
        File file = new File(tempFolder + File.separator + createPathByDate(date));
        if (!file.exists()) {
            file.mkdirs();
        }
        File fileWrite = new File(file.getPath() + File.separator + fileName);
        try (FileOutputStream fos = new FileOutputStream(fileWrite)) {
            fos.write(bytes);
        }
        return file.getPath() + File.separator + fileName;
    }

    /**
     * Save file zip
     *
     * @param mapFile
     * @return
     */
    public static byte[] saveTempFileZip(Map<String, byte[]> mapFile) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(outputStream);
        for (Map.Entry<String, byte[]> mapEntry : mapFile.entrySet()) {
            ZipEntry zipEntry = new ZipEntry(mapEntry.getKey());
            zipEntry.setSize(mapEntry.getValue().length);
            zos.putNextEntry(zipEntry);
            zos.write(mapEntry.getValue());
            zos.closeEntry();
        }
        zos.close();
        byte[] bytes = outputStream.toByteArray();
        outputStream.flush();
        outputStream.close();
        return bytes;
    }

    /**
     * Create path by date
     *
     * @param date
     * @return
     */
    public static String createPathByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String pathByDate = year + "/"
                + (month < 10 ? "0" + month : month) + "/"
                + (day < 10 ? "0" + day : day);
        return pathByDate;
    }

    /**
     * Create file name
     *
     * @param originalFilename
     * @param date
     * @return
     */
    public static String createFileName(String originalFilename, Date date) {
        originalFilename = originalFilename.replaceAll("[\\\\/:*?\"<>|%]", "");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return originalFilename + "_" + calendar.get(Calendar.HOUR_OF_DAY) + ""
                + calendar.get(Calendar.MINUTE) + "" + calendar.get(Calendar.SECOND)
                + "" + calendar.get(Calendar.MILLISECOND);
    }

    public static String getFilePath(String fullPath) {
        if (isStringNotBlank(fullPath)) {
            fullPath = fullPath
                    .replaceAll("[/\\\\]+", Matcher.quoteReplacement(System.getProperty("file.separator")));
            return FilenameUtils.getPath(fullPath);
        }
        return null;
    }

    public static String getFileName(String fullPath) {
        if (isStringNotBlank(fullPath)) {
            fullPath = fullPath
                    .replaceAll("[/\\\\]+", Matcher.quoteReplacement(System.getProperty("file.separator")));
            return FilenameUtils.getName(fullPath);
        }
        return null;
    }

    public static String getFileNameIgnoreExtension(String fullFileName) {
        int l = fullFileName.lastIndexOf('.');
        return fullFileName.substring(0, l);
    }

    public static String getFileExtension(String fullFileName) {
        int l = fullFileName.lastIndexOf('.');
        return fullFileName.substring(l + 1);
    }

    /**
     * Dinh dang tien te
     *
     * @param currency
     * @return
     */
    public static String formatVNDCurrency(Double currency) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.###");
        if (currency == null) {
            currency = new Double(0);
        }
        return formatter.format(currency);
    }

    /**
     * Dinh dang tien te
     *
     * @param currency
     * @return
     */
    public static String formatVNDCurrency(Long currency) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.###");
        if (currency == null) {
            currency = new Long(0);
        }
        return formatter.format(currency);
    }

    public static String convertLowerParamContains(String value) {
        String result = value.trim().toLowerCase()
                .replace("\\", "\\\\")
                .replaceAll("%", "\\\\%")
                .replaceAll("_", "\\\\_");
        return "%" + result + "%";
    }

    public static <T> List<T> pagingList(List<T> data, Integer fromIndex, Integer pageSize) {
        if (fromIndex == null || pageSize == null) {
            return data;
        }

        if (data == null || data.size() <= fromIndex) {
            return new ArrayList<>();
        }

        // toIndex exclusive
        return data.subList(fromIndex, Math.min(fromIndex + pageSize, data.size()));
    }

    private static String round(double input) {
        return String.valueOf(Math.round(input));
    }

    public static List<String> splitString(String s, String c) {
        List<String> rs = new ArrayList<>();

        String[] split = s.split(c, -1);
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].trim();
            if (StringUtils.isNotBlank(split[i])) {
                rs.add(split[i]);
            }
        }

        return rs;
    }

    public static String getImesTraceReq() {
        try {
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            if (httpServletRequest.getAttribute("imes-trace-req") != null) {
                return (String) httpServletRequest.getAttribute("imes-trace-req");
            }
        } catch (Exception exception) {

        }

        return "";
    }
}
