package cn.iocoder.yudao.module.infra.service.codegen.inner.generator;

/**
 * 系统词典数据生成器
 *
 * @author https://github.com/liyupi
 */
public class DictDataGenerator implements DataGenerator {
    /*private static final DictDataService dictService = SpringUtil.getBean(DictDataService.class);

    private final static Gson GSON = new Gson();

    @Override
    public List<String> doGenerate(CodegenColumnDO field, int rowNum) {
        String mockParams = field.getMockParams();
        long id = Long.parseLong(mockParams);
        Dict dict = dictService.getById(id);
        if (dict == null) {
            throw Exception(INTE);
        }
        List<String> wordList = GSON.fromJson(dict.getContent(),
                new TypeToken<List<String>>() {
                }.getType());
        List<String> list = new ArrayList<>(rowNum);
        for (int i = 0; i < rowNum; i++) {
            String randomStr = wordList.get(RandomUtils.nextInt(0, wordList.size()));
            list.add(randomStr);
        }
        return list;
    }*/
}
