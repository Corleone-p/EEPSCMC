from drs import drs
import sys
import numpy as np

"""根据传入参数，调用DRS进行目标利用率的生成"""


def imcpa_fun(totalProcessor, totalTask, targetU, cp, cf, xf):
    nHI = int(totalTask * cp)
    nLO = totalTask - nHI
    ULO = totalProcessor * targetU
    ULOLO = ULO * (1 - cp)
    ULOHI = ULO - ULOLO
    UHILO = xf * ULOLO
    UHIHI = cf * ULOHI
    vec_UHIHI_upbound = np.ones(nHI)
    vec_UHIHI = drs(nHI, UHIHI, vec_UHIHI_upbound)
    vec_ULOHI = drs(nHI, ULOHI, vec_UHIHI)
    vec_ULOLO_upBound = np.ones(nLO)
    vec_ULOLO = drs(nLO, ULOLO, vec_ULOLO_upBound)
    vec_UHILO = drs(nLO, UHILO, vec_ULOLO)

    print(vec_UHIHI)
    print(vec_ULOHI)
    print(vec_UHILO)
    print(vec_ULOLO)


"""从本地系统中获得传入参数"""
if __name__ == '__main__':
    totalProcessor = sys.argv[1]
    totalTask = sys.argv[2]
    targetU = sys.argv[3]
    cp = sys.argv[4]
    cf = sys.argv[5]
    xf = sys.argv[6]
    imcpa_fun(int(totalProcessor), int(totalTask), float(targetU), float(cp), float(cf), float(xf))
