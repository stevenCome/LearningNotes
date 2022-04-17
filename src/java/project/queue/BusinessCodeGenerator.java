package project.queue;

import com.jd.common.queue.LoadableMultiQueue;
import com.jd.tms.tfc.constant.Constants;
import com.jd.tms.tfc.constant.SeqConstant;
import com.jd.tms.tfc.manager.SysSequenceFeeManager;
import com.jd.tms.tfc.manager.SysSequenceManager;
import com.jd.tms.tfc.rpc.RedisClientProxy;
import com.jd.tms.tfc.util.Tools;
import com.jd.traceholder.TraceHolder;
import com.jd.ump.annotation.JProEnum;
import com.jd.ump.annotation.JProfiler;
import com.jd.ump.profiler.CallerInfo;
import com.jd.ump.profiler.proxy.Profiler;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 业务编码生成器
 *
 * @author suihonghua
 *
 */
public class BusinessCodeGenerator {

    private static final Logger logger = LoggerFactory.getLogger(BusinessCodeGenerator.class);

    private static final String PREFIX_REDIS_KEY = BusinessCodeGenerator.class.getName();

    private SimpleDateFormat simpleDateFormat6 = new SimpleDateFormat("yyMMdd");

    @Resource
    private SysSequenceManager sysSequenceManager;

    @Resource
    private SysSequenceFeeManager sysSequenceFeeManager;

    @Resource
    private RedisClientProxy redisClientProxy;

    @Resource
    private LoadableMultiQueue<Long> longMemoryLoadableMultiQueue;

    @Resource
    private LoadableMultiQueue<Long> longJimdbLoadableMultiQueue;

    @Resource
    private ApplicationConfig applicationConfig;

    /**
     * 运输需求单号前缀
     */
    public static final String PREFIX_TN = "TN";

    /**
     * 运输计划单号前缀
     */
    public static final String PREFIX_TP = "TP";

    /**
     * 运输任务单号前缀
     */
    public static final String PREFIX_TJ = "TJ";

    /**
     * 派车任务单号前缀
     */
    public static final String PREFIX_TW = "TW";

    /**
     * 委托书单号前缀
     */
    public static final String PREFIX_TB = "TB";
    /**
     * 车辆检查单号前缀
     */
    public static final String PREFIX_VC = "VC";
    /**
     * 运输需求模板前缀
     */
    public static final String PREFIX_NM = "NM";

    /**
     * 异常前缀
     */
    public static final String PREFIX_YC = "YC";
    /**
     * 结算平台运营数据对账单号前缀
     */
    public static final String PREFIX_TF = "TF";
    /**
     * 结算平台运营数据对账异常明细编码前缀
     */
    public static final String PREFIX_TA = "TA";
    /**
     * 卡班调度单前缀
     */
    public static final String PREFIX_PC = "PC";
    /**
     * 调度单前缀
     */
    public static final String PREFIX_TS = "TS";
    /**
     * 模板编号
     */
    public static final String PREFIX_TM = "TM";
    /**
     * 模板编号
     */
    public static final String PREFIX_TIB = "TIB";
    /**
     * 其它系统生成委托书的情况；
     */
    public static final String PREFIX_TB_O = "TBO";

    /**
     * 行车日志编码前缀
     */
    public static final String PREFIX_VL = "VL";

    public static final String PREFIX_CS = "CS";

    /**
     * 运输需求单号（TN + 日期(6位数字)+8位数字）
     * eg: TN14061900000001
     *
     * @author suihonghua
     * @return
     * @
     */
    @JProfiler(jKey = "tfc.businessCodeGenerator.genTransNeedCode", jAppName = Constants.UMP_APP, mState = { JProEnum.TP, JProEnum.FunctionError })
    public String genTransNeedCode(){
        return this.genSimpleFormat6CodeFromMemoryQueue(PREFIX_TN, new Date(), SeqConstant.SEQ_TRANS_NEED_BILL, 8);
    }
    /**
     * 运输计划单号（TP + 日期(6位数字)+8位数字）
     * eg: TP14061900000001
     *
     * @author suihonghua
     * @return
     *
     */
    @JProfiler(jKey = "tfc.businessCodeGenerator.genTransPlanCode", jAppName = Constants.UMP_APP, mState = { JProEnum.TP, JProEnum.FunctionError })
    public String genTransPlanCode(){
        return this.genSimpleFormat6CodeFromMemoryQueue(PREFIX_TP, new Date(), SeqConstant.SEQ_TRANS_PLAN_BILL, 8);
    }
    /**
     * 运输任务单号（TJ + 日期(6位数字)+8位数字）
     * eg: TJ14061900000001
     *
     * @author suihonghua
     * @return
     * @
     */
    @JProfiler(jKey = "tfc.businessCodeGenerator.genTransJobCode", jAppName = Constants.UMP_APP, mState = { JProEnum.TP, JProEnum.FunctionError })
    public String genTransJobCode(){
        return this.genSimpleFormat6CodeFromMemoryQueue(PREFIX_TJ, new Date(), SeqConstant.SEQ_TRANS_JOB_BILL, 8);
    }

    /**
     * 生成运输任务明细编号
     * @param transJobCode
     * @param index
     * @return
     * @
     */
    public String genTransJobItemCode(String transJobCode, int index){
        return transJobCode + "-" + Tools.formatNum(index, 3, false);//运输任务单号-3位序号
    }

    /**
     * 派车任务单号（TW + 日期(6位数字)+8位数字）
     * eg: TW14061900000001
     *
     * @author suihonghua
     * @return
     * @
     */
    @JProfiler(jKey = "tfc.businessCodeGenerator.genTransWorkCode", jAppName = Constants.UMP_APP, mState = { JProEnum.TP, JProEnum.FunctionError })
    public String genTransWorkCode(){
        return this.genSimpleFormat6CodeFromMemoryQueue(PREFIX_TW, new Date(), SeqConstant.SEQ_TRANS_WORK_BILL, 8);
    }

    /**
     * 生成运输任务明细编号
     * @param transWorkCode
     * @param index
     * @return
     * @
     */
    public String genTransWorkItemCode(String transWorkCode, int index){
        return transWorkCode + "-" + Tools.formatNum(index, 3, false);//运输任务单号-3位序号
    }

    /**
     * 委托书单号（TB + 日期(6位数字)+8位数字）
     * eg: TB14061900000001
     *
     * @author suihonghua
     * @return
     * @
     */
    @JProfiler(jKey = "tfc.businessCodeGenerator.genTransBookCode", jAppName = Constants.UMP_APP, mState = { JProEnum.TP, JProEnum.FunctionError })
    public String genTransBookCode(){
        return this.genSimpleFormat6CodeFromMemoryQueue(PREFIX_TB, new Date(), SeqConstant.SEQ_TRANS_BOOK_BILL, 8);
    }

    /**
     * 委托书单号（TBO + 日期(6位数字)+8位数字）
     * eg: TBO14061900000001
     * @return
     * @
     */
    @JProfiler(jKey = "tfc.businessCodeGenerator.genTransBookCodeOther", jAppName = Constants.UMP_APP, mState = { JProEnum.TP, JProEnum.FunctionError })
    public String genTransBookCodeOther(){
        return this.genSimpleFormat6CodeFromMemoryQueue(PREFIX_TB_O, new Date(), SeqConstant.SEQ_TRANS_BOOK_BILL_OTHER, 8);
    }

    /**
     * 车辆检查单编号
     * @return
     * @
     */
    @JProfiler(jKey = "tfc.businessCodeGenerator.genVehicleCheckCode", jAppName = Constants.UMP_APP, mState = { JProEnum.TP, JProEnum.FunctionError })
    public String genVehicleCheckCode(){
        return this.genSimpleFormat6CodeFromMemoryQueue(PREFIX_VC, new Date(), SeqConstant.SEQ_TRANS_VEHICLE_CHECK, 8);
    }

    /**
     * 生成派车任务明细简码
     * @return
     * @
     */
    @JProfiler(jKey = "tfc.businessCodeGenerator.genTransWorkItemSimpleCode", jAppName = Constants.UMP_APP, mState = { JProEnum.TP, JProEnum.FunctionError })
    public String genTransWorkItemSimpleCode(){
        int initialValue = 10000000;
        int maxValue = 99999999;
        long seq = this.sysSequenceManager.selectNextValueInRange(
                determineCurrentLookupKey(SeqConstant.SEQ_TRANS_WORK_ITEM_SIMPLE_CODE), initialValue, maxValue);
        return String.valueOf(seq);
        //return this.genSimpleFormat6CodeFromDB("", new Date(), SeqConstant.SEQ_TRANS_WORK_ITEM_SIMPLE_CODE, 8);
    }

    @JProfiler(jKey = "tfc.businessCodeGenerator.genTransWorkItemSimpleCode14", jAppName = Constants.UMP_APP, mState = { JProEnum.TP, JProEnum.FunctionError })
    public String genTransWorkItemSimpleCode14(){
        return this.genSimpleFormat6CodeFromMemoryQueue("", new Date(), SeqConstant.SEQ_TRANS_WORK_ITEM_SIMPLE_CODE, 8);
    }

    /**
     * 生成运输线路调度任务编码
     * @param modelCode  运输模板编码
     * @return
     * @
     */
    public String genScheduleTransTaskCode(String modelCode, Date date) {
        if(StringUtils.isBlank(modelCode) || date == null){
            throw new IllegalArgumentException("Parameter cannot be empty");
        }
        return modelCode + "-" + this.simpleDateFormat6.format(date);
    }

    /**
     * 生成运输线路调度任务线路编码
     * @param transTaskCode  运输线路编码
     * @index 序号
     * @return
     * @
     */
    public String genScheduleTransTaskLineCode(String transTaskCode, int index) {
        if(StringUtils.isBlank(transTaskCode) || index <= 0){
            throw new IllegalArgumentException("Parameter cannot be empty");
        }
        return transTaskCode + "-" + Tools.formatNum(index, 3, false);
    }

    /**
     * 生成方案域编码
     * @param transTaskCode  任务编码
     * @index 序号
     * @return
     */
    public String genScheduleTransSchemeDomainCode(String transTaskCode, int index) {
        if(StringUtils.isBlank(transTaskCode) || index <= 0){
            throw new IllegalArgumentException("Parameter cannot be empty");
        }
        return transTaskCode + "-" + Tools.formatNum(index, 3, false);
    }

    /**
     * 生成运输线路调度方案编码
     * @param domainCode  方案域编码
     * @return
     * @
     */
    public String genScheduleTransSchemeCode(String domainCode, int index) {
        if(StringUtils.isBlank(domainCode)){
            throw new IllegalArgumentException("Parameter cannot be empty");
        }
        return domainCode + "-" + Tools.formatNum(index, 4, true);
    }

    /**
     * 生成运输需求模板编码
     * @return
     */
    public String genTransNeedModelCode() {
        return this.genSimpleFormat6CodeFromDB(PREFIX_NM, new Date(), SeqConstant.SEQ_TRANS_NEED_MODEL, 8);
    }

    /**
     * 结算平台运营数据对账单号（TF + 日期(6位数字)+8位数字）
     * eg: TF14061900000001
     *
     * @author lishengtao
     * @return
     * @
     */
    @JProfiler(jKey = "tfc.businessCodeGenerator.genTransFeeBillCode", jAppName = Constants.UMP_APP, mState = { JProEnum.TP, JProEnum.FunctionError })
    public String genTransFeeBillCode(){
        return this.genSimpleFormat6CodeFromFeeDB(PREFIX_TF, new Date(), SeqConstant.SEQ_TRANS_FEE_BILL, 8);
    }

    /**
     * 结算平台运营数据对账异常明细编码（TF + 日期(6位数字)+8位数字）
     * eg: TA14061900000001
     *
     * @author zhangyongqi
     * @return
     * @
     */
    @JProfiler(jKey = "tfc.businessCodeGenerator.genAbnormalCode", jAppName = Constants.UMP_APP, mState = { JProEnum.TP, JProEnum.FunctionError })
    public String genAbnormalCode(){
        return this.genSimpleFormat6CodeFromFeeDB(PREFIX_TA, new Date(), SeqConstant.SEQ_TRANS_FEE_ABNORMAL_DETAIL, 8);
    }

    @JProfiler(jKey = "tfc.businessCodeGenerator.genTransInsurantBillCode", jAppName = Constants.UMP_APP, mState = { JProEnum.TP, JProEnum.FunctionError })
    public String genTransInsurantBillCode(){
        return this.genSimpleFormat6CodeFromDB(PREFIX_TIB, new Date(), SeqConstant.SEQ_TRANS_NEED_BILL, 8);
    }

    /**
     * 生成业务单号266格式（XX + 年份月份日期（6位）+ 流水号（[seqLength]位））- （从db获取）
     *
     * @author suihonghua
     * @param prefix
     * @param date6
     * @param seqKey
     * @param seqLength
     * @return
     * @
     */
    private String genSimpleFormat6CodeFromDB(String prefix, Date date6, String seqKey, int seqLength) {
        long seq = this.getSequenceFromDB(seqKey);
        return this.formatSimpleFormat6Code(prefix, date6, seq, seqLength);
    }



    private String genSimpleFormat6CodeFromFeeDB(String prefix, Date date6, String seqKey, int seqLength) {
        long seq = this.getSequenceFromFeeDB(seqKey);
        return this.formatSimpleFormat6Code(prefix, date6, seq, seqLength);
    }


    /**
     * 生成业务单号26x格式（XX + 年份月份日期（6位）+ 流水号（[seqLength]位））- （从redis获取）
     *
     * @author suihonghua
     * @param prefix
     * @param date6
     * @param seqKey
     * @param seqLength
     * @return
     * @
     */
    private String genSimpleFormat6CodeFromRedis(String prefix, Date date6, String seqKey, int seqLength) {
        long seq = this.getSequenceFromRedis(seqKey);
        return this.formatSimpleFormat6Code(prefix, date6, seq, seqLength);
    }

    /**
     * 生成业务单号266格式（XX + 年份月份日期（6位）+ 流水号（[seqLength]位））- （从MemoryQueue获取）
     * @param prefix
     * @param date6
     * @param seqKey
     * @param seqLength
     * @return
     */
    public String genSimpleFormat6CodeFromMemoryQueue(String prefix, Date date6, String seqKey, int seqLength) {
        long seq = this.getSequenceFromMemoryQueue(seqKey);
        return this.formatSimpleFormat6Code(prefix, date6, seq, seqLength);
    }

    /**
     * 生成业务单号266格式（XX + 年份月份日期（6位）+ 流水号（[seqLength]位））- （从MemoryQueue获取）
     * @param prefix
     * @param date6
     * @param seqKey
     * @param seqLength
     * @return
     */
    public String genSimpleFormat6CodeFromJimQueue(String prefix, Date date6, String seqKey, int seqLength) {
        long seq = this.getSequenceFromJimQueue(seqKey);
        return this.formatSimpleFormat6Code(prefix, date6, seq, seqLength);
    }

    /**
     * 获取Sequence（DB）
     * @param seqKey
     * @return
     */
    @JProfiler(jKey = "tfc.businessCodeGenerator.getSequenceFromDB", jAppName = Constants.UMP_APP, mState = { JProEnum.TP, JProEnum.FunctionError })
    public long getSequenceFromDB(String seqKey){
        return this.sysSequenceManager.selectNextValue(determineCurrentLookupKey(seqKey));
    }


    /**
     *  获取Sequence（计费DB）
     * @param seqKey
     * @return
     */
    @JProfiler(jKey = "tfc.businessCodeGenerator.getSequenceFromFeeDB", jAppName = Constants.UMP_APP, mState = { JProEnum.TP, JProEnum.FunctionError })
    private long getSequenceFromFeeDB(String seqKey){
        return this.sysSequenceFeeManager.selectNextValue(determineCurrentLookupKey(seqKey));
    }


    /**
     * 获取Sequence（Redis）
     * @param seqKey
     * @return
     */
    private long getSequenceFromRedis(String seqKey){
        String key = PREFIX_REDIS_KEY + "-" + seqKey;
        return this.redisClientProxy.incr(determineCurrentLookupKey(key));
    }

    /**
     * 获取Sequence(MemoryQueue)
     * @param seqKey
     * @return
     */
    @JProfiler(jKey = "tfc.businessCodeGenerator.getSequenceFromMemoryQueue", jAppName = Constants.UMP_APP, mState = { JProEnum.TP, JProEnum.FunctionError })
    public long getSequenceFromMemoryQueue(String seqKey){
        return this.longMemoryLoadableMultiQueue.poll(determineCurrentLookupKey(seqKey));
    }

    /**
     * 获取Sequence(MemoryQueue)
     * @param seqKey
     * @return
     */
    @JProfiler(jKey = "tfc.businessCodeGenerator.getSequenceFromJimQueue", jAppName = Constants.UMP_APP, mState = { JProEnum.TP, JProEnum.FunctionError })
    public long getSequenceFromJimQueue(String seqKey){
        return this.longJimdbLoadableMultiQueue.poll(determineCurrentLookupKey(seqKey));
    }

    /**
     * 格式化编码26x格式（XX + 年份月份日期（6位）+ 流水号（[seqLength]位））
     * @param prefix
     * @param date6
     * @param seq
     * @param seqLength
     * @return
     */
    private String formatSimpleFormat6Code(String prefix, Date date6, long seq, int seqLength) {
        String date = this.simpleDateFormat6.format(date6);
        String code = prefix + date + this.formatNum(seq, seqLength, true);
        logger.info("formatSimpleFormat6Code: code["+code+"]");
        return code;
    }

    /**
     * 数字格式化
     * eg:
     *  formatNum(1234567890,8) returns "34567890"
     *  formatNum(1234567890,12) returns "001234567890"
     *
     * @author suihonghua
     * @param num
     * @param length
     * @param forceSub
     * @return
     *
     */
    private String formatNum(long num, int length, boolean forceSub) {
        String str = String.format("%0"+length+"d", num);
        if(forceSub){
            if(str.length() > length){
                str = str.substring(str.length() - length);
            }
        }
        return str;
    }

    //---------- setter and getter ----------//
    public void setSysSequenceManager(SysSequenceManager sysSequenceManager) {
        this.sysSequenceManager = sysSequenceManager;
    }

    /**
     * 异常单（TN + 日期(6位数字)+8位数字）
     * eg: TN14061900000001
     *
     * @author suihonghua
     * @return
     * @
     */
    public String genTransCarrierAbnormalCode(){
        return this.genSimpleFormat6CodeFromDB(PREFIX_YC, new Date(), SeqConstant.SEQ_TRANS_CARRIER_ABNORMAL, 8);
    }

    /**
     * 卡班调度单前缀（PC + 日期(6位数字)+8位数字）
     * eg: PC14061900000001
     *
     * @author suihonghua
     * @return
     * @
     */
    public String genScheduleCargoCode(){
        return this.genSimpleFormat6CodeFromDB(PREFIX_PC, new Date(), SeqConstant.SEQ_TRANS_SCHEDULE_CARGO, 8);
    }
    /**
     * 运输需求单号（TS + 日期(6位数字)+8位数字）
     * eg: TS14061900000001
     *
     * @author wangjing88
     * @return
     * @
     */
    public String genTransScheduleCode(){
        return this.genSimpleFormat6CodeFromDB(PREFIX_TS, new Date(), SeqConstant.SEQ_TRANS_SCHEDULE_BILL, 8);
    }
    /**
     * 生成调度单明细编号
     * @param transScheduleCode
     * @param index
     * @return
     * @
     */
    public String genTransScheduleItemCode(String transScheduleCode, int index){
        return transScheduleCode + "-" + Tools.formatNum(index, 3, false);//调度单号-3位序号
    }
    /**
     * 生成调度单派车单号
     * @param transScheduleCode
     * @param index
     * @return
     * @
     */
    public String genScheduleVehicleCodeCode(String transScheduleCode, int index){
        return transScheduleCode + "-V" + Tools.formatNum(index, 2, false);//调度单号-2位序号
    }
    /**
     * 任务调度模板号
     * @return
     * @
     */
    public String genTransScheduleModelCode(){
        return this.genSimpleFormat6CodeFromDB(PREFIX_TM, new Date(), SeqConstant.SEQ_TRANS_SCHEDULE_MODEL_BILL, 8);
    }
    /**
     * 生成调度单明细编号
     * @param transScheduleModelCode
     * @param index
     * @return
     * @
     */
    public String genTransScheduleModelItemCode(String transScheduleModelCode, int index){
        return transScheduleModelCode + "-" + Tools.formatNum(index, 3, false);//调度单号-3位序号
    }
    /**
     * 生成调度单派车单号
     * @param transScheduleModelCode
     * @param index
     * @return
     * @
     */
    public String genScheduleModelVehicleCode(String transScheduleModelCode, int index){
        return transScheduleModelCode + "-V" + Tools.formatNum(index, 2, false);//调度单号-2位序号
    }

    /**
     * 生成班次编码
     */
    public String genTransCalendarShiftCode(){
        return genSimpleFormat6CodeFromDB(PREFIX_CS, new Date(), SeqConstant.SEQ_TRANS_CALENDAR_SHIFT, 8);
    }

    /**
     * 行车日志编码（VL + 日期(6位数字)+8位数字）
     * eg: VL14061900000001
     * @return
     * @
     */
    @JProfiler(jKey = "tfc.businessCodeGenerator.getVehicleLogCode", jAppName = Constants.UMP_APP, mState = { JProEnum.TP, JProEnum.FunctionError })
    public String getVehicleLogCode(){
//		return this.genSimpleFormat6CodeFromDB(PREFIX_VL, new Date(), SeqConstant.SEQ_TRANS_VEHICLE_LOG, 8);
        return this.genSimpleFormat6CodeFromMemoryQueue(PREFIX_VL, new Date(), SeqConstant.SEQ_TRANS_VEHICLE_LOG, 8);
    }

    /**
     * 获取序列号
     * @return
     */
    private String determineCurrentLookupKey(String seqKey){
        /*压测流量增加前缀*/
		/*if(TraceHolder.isForcebot()){
			logger.info("压测流量获取序列号");
			return "shadow_" + seqKey;
		}*/
        return seqKey;
    }

}
