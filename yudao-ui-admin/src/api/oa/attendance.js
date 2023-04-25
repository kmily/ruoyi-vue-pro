import request from '@/utils/request'

// 创建考勤打卡
export function createAttendance(data) {
  return request({
    url: '/oa/attendance/create',
    method: 'post',
    data: data
  })
}

// 更新考勤打卡
export function updateAttendance(data) {
  return request({
    url: '/oa/attendance/update',
    method: 'put',
    data: data
  })
}

// 删除考勤打卡
export function deleteAttendance(id) {
  return request({
    url: '/oa/attendance/delete?id=' + id,
    method: 'delete'
  })
}

// 获得考勤打卡
export function getAttendance(id) {
  return request({
    url: '/oa/attendance/get?id=' + id,
    method: 'get'
  })
}

// 获得考勤打卡分页
export function getAttendancePage(query) {
  return request({
    url: '/oa/attendance/page',
    method: 'get',
    params: query
  })
}

// 导出考勤打卡 Excel
export function exportAttendanceExcel(query) {
  return request({
    url: '/oa/attendance/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
