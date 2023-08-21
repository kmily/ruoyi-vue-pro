import request from '@/utils/request'


// 查询用户家庭
export function getFamilyList(userId){
  return request({
    url: '/member/family/list?userId=' + userId,
    method: 'get',
  })
}


// 查询用户家庭
export function getFamilyDevicesList(userId){
  return request({
    url: '/member/device-user/family-devices?userId=' + userId,
    method: 'get',
  })
}