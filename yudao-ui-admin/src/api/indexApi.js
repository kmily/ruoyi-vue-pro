// 问候语
export function getGreeting() {
  //创建一个日期对象
  const dd = new Date();
  const hour = dd.getHours();//获取当前时

  let word = "";
  if (hour > 5 && hour <= 11) {//早上
    word = "一日之计在于晨，越早预约办理，越早通过！";
  } else if (hour > 11 && hour <= 14) {//中午
    word = "午休时间,您要保持睡眠哦！";
  } else if (hour > 14 && hour <= 19) {//下午
    word = "祝您下午工作愉快！";
  } else if (hour > 19 && hour <= 24 || hour > 0 && hour <= 5) {//晚上
    word = "还没休息呐，要注意身体哦！";
  } else {//凌晨
    word = "凌晨啦，注意肾哦！";
  }
  return word;
}
